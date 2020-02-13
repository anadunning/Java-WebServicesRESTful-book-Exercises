package br.com.livro.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.livro.domain.Carro;
import br.com.livro.domain.CarroService;
import br.com.livro.domain.ListaCarros;
import br.com.livro.domain.Response;
import br.com.livro.util.JAXBUtil;
import br.com.livro.util.RegexUtil;
import br.com.livro.util.ServletUtil;

@WebServlet("/carros/*")
public class CarrosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CarroService carroService = new CarroService();
	
//	@Override
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		List<Carro> carros = carroService.getCarros();
//		ListaCarros lista = new ListaCarros();
//		lista.setCarros(carros);
//		
//		// Gera o XML
//		String xml = JAXBUtil.toXML(lista);
//		
//		// Escreve o XML na response do servlet com application/xml
//		ServletUtil.writeXML(resp, xml);
//		
//		String carrosString = carros.toString();
//		resp.getWriter().write(carrosString);
//	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestUri = req.getRequestURI();
		Long id = RegexUtil.matchId(requestUri);
		if (id != null) {
			// Informou o id
			Carro carro = carroService.getCarro(id);
			if (carro != null) {
				String json = JAXBUtil.toJSON(carro);
				ServletUtil.writeJSON(resp, json);
			}
			else {
				// Lista de carros
				List<Carro> carros = carroService.getCarros();
				ListaCarros lista = new ListaCarros();
				lista.setCarros(carros);
				
				// Gera o JSON
				String json = JAXBUtil.toJSON(lista);
				
				// Escreve o JSON na response do servlet com application/json
				ServletUtil.writeJSON(resp, json);				
			}
		}
	}
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse resp) throws IOException, ServletException {
		// Cria o carro
		Carro carro = getCarroFromRequest(request);
		
		// Salva o carro
		carroService.save(carro);
		
		// Escreve o JSON do novo carro salvo
		List<Carro> carros = carroService.getCarros();
		ListaCarros lista = new ListaCarros();
		lista.setCarros(carros);
		
		// Gera o JSON
		String json = JAXBUtil.toJSON(lista);
		
		// Escreve o JSON na response do servlet com application/json
		ServletUtil.writeJSON(resp, json);	
	}
	
	// Le os parametros da request e cria o objeto Carro
	private Carro getCarroFromRequest(HttpServletRequest request) {
		Carro c = new Carro();
		String id = request.getParameter("id");
		if (id != null) {
			// Se informou o id, busca o objeto do banco de dados
			c = carroService.getCarro(Long.parseLong(id));
		}
		c.setNome(request.getParameter("nome"));
		c.setDesc(request.getParameter("descricao"));
		c.setUrlFoto(request.getParameter("url_foto"));
		c.setUrlVideo(request.getParameter("url_video"));
		c.setLatitude(request.getParameter("latitude"));
		c.setLongitude(request.getParameter("longitude"));
		c.setTipo(request.getParameter("tipo"));
		return c;
	}
	
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestUri = req.getRequestURI();
		Long id = RegexUtil.matchId(requestUri);
		if (id != null) {
			carroService.delete(id);
			Response r = Response.Ok("Carro excluido com sucesso");
			String json = JAXBUtil.toJSON(r);
			ServletUtil.writeJSON(resp, json);
		}
		else {
			// URL invalida
			resp.sendError(404, "URL invalida");
		}
	}
}