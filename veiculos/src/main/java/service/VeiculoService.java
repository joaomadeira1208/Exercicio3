package service;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import dao.VeiculoDAO;
import model.Veiculo;
import spark.Request;
import spark.Response;


public class VeiculoService {

	private VeiculoDAO veiculoDAO = new VeiculoDAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_MARCA = 2;
	private final int FORM_ORDERBY_ANO = 3;
	
	
	public VeiculoService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Veiculo(), FORM_ORDERBY_MARCA);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Veiculo(), orderBy);
	}

	
	public void makeForm(int tipo, Veiculo veiculo, int orderBy) {
		String nomeArquivo = "index.html";
		form = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	form += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umVeiculo = "";
		if(tipo != FORM_INSERT) {
			umVeiculo += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umVeiculo += "\t\t<tr>";
			umVeiculo += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/veiculo/list/1\">Novo Veiculo</a></b></font></td>";
			umVeiculo += "\t\t</tr>";
			umVeiculo += "\t</table>";
			umVeiculo += "\t<br>";			
		}
		
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/veiculo/";
			String name, marca, buttonLabel;
			if (tipo == FORM_INSERT){
				action += "insert";
				name = "Inserir Veiculo";
				marca = "porshe, ferrari, ...";
				buttonLabel = "Inserir";
			} else {
				action += "update/" + veiculo.getID();
				name = "Atualizar Veiculo (ID " + veiculo.getID() + ")";
				marca = veiculo.getMarca();
				buttonLabel = "Atualizar";
			}
			umVeiculo += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
			umVeiculo += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umVeiculo += "\t\t<tr>";
			umVeiculo += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
			umVeiculo += "\t\t</tr>";
			umVeiculo += "\t\t<tr>";
			umVeiculo += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umVeiculo += "\t\t</tr>";
			umVeiculo += "\t\t<tr>";
			umVeiculo += "\t\t\t<td>&nbsp;Marca: <input class=\"input--register\" type=\"text\" name=\"marca\" value=\""+ marca +"\"></td>";
			umVeiculo += "\t\t\t<td>Modelo: <input class=\"input--register\" type=\"text\" name=\"modelo\" value=\""+ veiculo.getModelo() +"\"></td>";
			umVeiculo += "\t\t\t<td>Ano: <input class=\"input--register\" type=\"text\" name=\"ano\" value=\""+ veiculo.getAno() +"\"></td>";
			umVeiculo += "\t\t\t<td>Quilometragem: <input class=\"input--register\" type=\"text\" name=\"quilometragem\" value=\""+ veiculo.getQuilometragem() +"\"></td>";
			umVeiculo += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
			umVeiculo += "\t\t</tr>";
			umVeiculo += "\t</table>";
			umVeiculo += "\t</form>";		
		} else if (tipo == FORM_DETAIL){
			umVeiculo += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umVeiculo += "\t\t<tr>";
			umVeiculo += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Veiculo (ID " + veiculo.getID() + ")</b></font></td>";
			umVeiculo += "\t\t</tr>";
			umVeiculo += "\t\t<tr>";
			umVeiculo += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umVeiculo += "\t\t</tr>";
			umVeiculo += "\t\t<tr>";
			umVeiculo += "\t\t\t<td>&nbsp;Marca: "+ veiculo.getMarca() +"</td>";
			umVeiculo += "\t\t\t<td>Modelo: "+ veiculo.getModelo() +"</td>";
			umVeiculo += "\t\t\t<td>Ano: "+ veiculo.getAno() +"</td>";
			umVeiculo += "\t\t\t<td>Quilometragem: "+ veiculo.getQuilometragem() +"</td>";
			umVeiculo += "\t\t</tr>";
			umVeiculo += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<UM-VEICULO>", umVeiculo);
		
		String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Veiculos</b></font></td></tr>\n" +
				"\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td><a href=\"/veiculo/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
        		"\t<td><a href=\"/veiculo/list/" + FORM_ORDERBY_MARCA + "\"><b>Marca</b></a></td>\n" +
        		"\t<td><a href=\"/veiculo/list/" + FORM_ORDERBY_ANO + "\"><b>Ano</b></a></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		List<Veiculo> veiculos;
		if (orderBy == FORM_ORDERBY_ID) {                 	veiculos = veiculoDAO.getOrderByID();
		} else if (orderBy == FORM_ORDERBY_MARCA) {		veiculos = veiculoDAO.getOrderByMarca();
		} else if (orderBy == FORM_ORDERBY_ANO) {			veiculos = veiculoDAO.getOrderByAno();
		} else {											veiculos = veiculoDAO.get();
		}

		int i = 0;
		String bgcolor = "";
		for (Veiculo v : veiculos) {
			bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td>" + v.getID() + "</td>\n" +
            		  "\t<td>" + v.getMarca() + "</td>\n" +
            		  "\t<td>" + v.getAno() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/veiculo/" + v.getID() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/veiculo/update/" + v.getID() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteVeiculo('" + v.getID() + "', '" + v.getMarca() + "', '" + v.getAno() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		form = form.replaceFirst("<LISTAR-VEICULO>", list);				
	}
	
	
	public Object insert(Request request, Response response) {
		String marca = request.queryParams("marca");
		String modelo = request.queryParams("modelo");
		int ano = Integer.parseInt(request.queryParams("ano"));
		float quilometragem = Float.parseFloat(request.queryParams("quilometragem"));
		String resp = "";
		
		Veiculo veiculo = new Veiculo(-1, marca, modelo, ano, quilometragem);
		
		if(veiculoDAO.insert(veiculo) == true) {
            resp = "Veiculo da marca(" + marca + ") inserido!";
            response.status(201); // 201 Created
		} else {
			resp = "Produto da marca(" + marca + ") não inserido!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Veiculo veiculo = (Veiculo) veiculoDAO.get(id);
		
		if (veiculo != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, veiculo, FORM_ORDERBY_MARCA);
        } else {
            response.status(404); // 404 Not found
            String resp = "Veiculo " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}

	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Veiculo veiculo = (Veiculo) veiculoDAO.get(id);
		
		if (veiculo != null) {
			response.status(200); // success
			makeForm(FORM_UPDATE, veiculo, FORM_ORDERBY_MARCA);
        } else {
            response.status(404); // 404 Not found
            String resp = "Veiculo " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return form;
	}			
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Veiculo veiculo = veiculoDAO.get(id);
        String resp = "";       

        if (veiculo != null) {
        	veiculo.setMarca(request.queryParams("marca"));
        	veiculo.setModelo(request.queryParams("modelo"));
        	veiculo.setAno(Integer.parseInt(request.queryParams("ano")));
        	veiculo.setQuilometragem(Float.parseFloat(request.queryParams("quilometragem")));
        	veiculoDAO.update(veiculo);
        	response.status(200); // success
            resp = "Veiculo (ID " + veiculo.getID() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Veiculo (ID \" + veiculo.getId() + \") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Veiculo veiculo = veiculoDAO.get(id);
        String resp = "";       

        if (veiculo != null) {
            veiculoDAO.delete(id);
            response.status(200); // success
            resp = "Veiculo (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Veiculo (" + id + ") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
}