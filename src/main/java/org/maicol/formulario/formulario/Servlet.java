package org.maicol.formulario.formulario;

/*Nombre del programador: Michael Guaman
Materia: Lenguajes de Programacion 2
Fecha: 20/05/2024
Detalle: Carrito de compras
Este formulario permite el ingreso de datos de un producto
y a su vez permite mostrar un sistema de facturacion
Version:1.1.0*/

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Permite crear una sesion para almacenar la informacion ingresada por el usuario
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();

        // Ingreso de los datos o parametros que se necesita para el formulario
        String nombre = request.getParameter("nombre");
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));
        double valorUnitario = Double.parseDouble(request.getParameter("valorUnitario"));

        // Calcular el valor total de los productos ingresados
        double valorTotal = cantidad * valorUnitario;

        // Obtener los datos de la factura que se almacenan en la sesion
        String factura = (String) session.getAttribute("factura");
        if (factura == null) {
            factura = "<table><tr><th>Producto</th><th>Cantidad</th><th>Valor Unitario</th><th>Subtotal</th></tr>";
        }

        // Obtener el total de todas las compras realizadas en la sesion
        Double totalFin = (Double) session.getAttribute("totalFin");
        if (totalFin == null) {
            totalFin = 0.0;
        }

        // Agregar los datos ingresados a la factura
        factura += "<tr><td>" + nombre + "</td><td>" + cantidad + "</td><td>" + valorUnitario + "</td><td>" + valorTotal + "</td></tr>";

        // Actualizar el total acumulado
        totalFin += valorTotal;

        // Actualizar los datos de la factura y el total en la sesion
        session.setAttribute("factura", factura);
        session.setAttribute("totalFin", totalFin);

        // Generar la respuesta HTML para mostrar la factura
        PrintWriter out = response.getWriter();
        out.println("<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\"></head><body>");
        out.println("<h1>Factura de los productos</h1>");

        // Calculo del IVA (15%)
        double iva = totalFin * 0.15;
        double totalConIVA = totalFin + iva;

        // Mostrar la factura
        if (!factura.isEmpty()) {
            out.println(factura);
            out.println("<tr><td colspan='3'>Total General</td><td>" + totalFin + "</td></tr>");
            out.println("<tr><td colspan='3'>IVA (15%)</td><td>" + iva + "</td></tr>");
            out.println("<tr><td colspan='3'>Total con IVA</td><td>" + totalConIVA + "</td></tr>");
            out.println("</table>");
        } else {
            out.println("<p>Usted no adquirio productos</p>");
        }

        // Botón para regresar al formulario
        out.println("<form action='index.html' method='get'>");
        out.println("<input type='submit' value='Regresar'>");
        out.println("</form>");
        out.println("</body></html>");
    }
}
