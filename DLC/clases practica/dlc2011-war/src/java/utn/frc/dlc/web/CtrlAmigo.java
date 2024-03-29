/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utn.frc.dlc.web;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utn.frc.dlc.base.Persona;
import utn.frc.dlc.db.SqlManager;
import utn.frc.dlc.web.aux.Aux;

import utn.frc.dlc.web.db.DBAmigo;

public class CtrlAmigo extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
         RequestDispatcher disp;
        ServletContext app = this.getServletContext();
        String dest = "/amigos.jsp";

        String fecha = Aux.getFecha("dd 'de' MMMM 'de' yyyy, H:mm 'hs'");
        request.getSession().setAttribute("fecha", fecha);

        Persona usr = (Persona) request.getSession().getAttribute("usr");
        if (usr==null) {
            usr = Aux.initUsr();
            request.getSession().setAttribute("usr", usr);
        }

        SqlManager sql = (SqlManager) request.getSession().getAttribute("sql");
        if (sql==null) {
            sql = Aux.initSql();
            request.getSession().setAttribute("sql", sql);
        }
        request.getSession().setAttribute("nombre", request.getParameter("nombre"));
         request.getSession().setAttribute("id", request.getParameter("id"));
        int id = Integer.parseInt(request.getParameter("id"));
        String query = "SELECT * FROM usuario u, amigo a where u.idUsuario = a.idAmigo2 AND idAmigo1 = "+ id ;
        String errorMsg = null;
        try {
            errorMsg = "Error en la conexión.";
            sql.connect();
            errorMsg = "Error al cargar los amigos.";

              List amigos = DBAmigo.loadAmigos(sql,query);
            sql.close();
           
             request.getSession().setAttribute("amigos", amigos);
        } catch (Exception e) {
            errorMsg += e.getMessage();
            request.getSession().setAttribute("errorMsg", errorMsg);
            dest = "/error.jsp";
        }

        disp = app.getRequestDispatcher(dest);
        disp.forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
