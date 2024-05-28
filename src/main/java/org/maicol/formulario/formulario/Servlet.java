package org.maicol.formulario.formulario;

/*Nombre del programador: Michael Guaman
Materia: Lenguajes de Programacion 2
Fecha: 20/05/2024
Detalle:Carrito de compras
Este formulario permite el ingreso de datos de un producto
y a su vez permite mostrar un sistema de facturacion
Version:1.1.0*/

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Permite crear un sesion para almacenar la informacion ingresada por el usuario
        HttpSession session = request.getSession();

        //Ingreso de los datos o parametros que se necesita para el formulario
        String nombre = request.getParameter("nombre");
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));
        double valorUnitario = Double.parseDouble(request.getParameter("valorUnitario"));

        // Calcular el valor total de los productos ingresados
        double valorTotal = cantidad * valorUnitario;

        // Aqui se obtiene los datos de la factura que se almacenan en la sesion
        String factura = (String) session.getAttribute("factura");
        if (factura == null) {
            factura = "";
        }

        //Aqui se obtiene el total de todas las compras realizadas en la sesion, es decir se acumulan
        //cada que ingresan nuevos productos
        Double totalFin = (Double) session.getAttribute("totalFin");
        if (totalFin == null) {
            totalFin = 0.0;
        }

        // Se agregan los los datos ingresados a la factura
        factura += "<p>Su producto: " + nombre + "<br>";
        factura += "La cantidad que adquirio es: " + cantidad + "<br>";
        factura += "El valor Unitario es: " + valorUnitario + "<br>";
        factura += "El subtotal es: " + valorTotal + "</p>";

        // Actualizar el total acumulado es decir el total de todas las compras
        // con el valor total del producto actual
        totalFin += valorTotal;

        //Aqui se actualizan los datos de la factura nueva que ingresa el uusuario cada que inicia sesion
        //y a su vez se actuliza el campo del total de todos los productos acumulados
        session.setAttribute("factura", factura);
        session.setAttribute("totalFin", totalFin);

        //Se redirige al formulario para ingresar nuevos productos, aqui podemos utilizar el metodo get o set
        //dependiendo su caso
        response.sendRedirect("Servlet");
    }

    //mediante el metodo doGet enviamos maneja las distintas peticiones enviadas al servlet
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        HttpSession session = request.getSession();
        //obtenemos la factura actula de los productos ingresados y el total acumulado de los productos
        String factura = (String) session.getAttribute("factura");
        Double totalFin = (Double) session.getAttribute("totalFin");

        //Calculo del IVA (15%)
        double iva = totalFin * 0.15;

        // Calcular el total con el IVA incluido
        double totalConIVA = totalFin + iva;

        // Generar la respuesta de acuerdo a la estructura del HTML para mostrar
        //los datos de la factura
        response.setContentType("text/html");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>Factura de los productos</h1>");

        //colocamos una condicion en mostrando el total acumulado de todas las compras realizadas
        //caso contrario se enviara un mensaje de que no contien productos
        if (factura != null && !factura.isEmpty()) {
            response.getWriter().println(factura);
            response.getWriter().println("<p>IVA (15%): " + iva + "</p>");
            response.getWriter().println("<p>Total de su compra " + totalConIVA + "</p>");
        } else {
            response.getWriter().println("<p>Usted no adquirio productos</p>");
        }

        response.getWriter().println("<form action='index.html' method='get'>");
        response.getWriter().println("<input type='submit' value='Regresar'>");
        response.getWriter().println("</form>");
        response.getWriter().println("</body></html>");
    }
}
