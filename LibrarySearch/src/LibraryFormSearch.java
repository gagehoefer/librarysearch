import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LibraryFormSearch")
public class LibraryFormSearch extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public LibraryFormSearch() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String search_booktitle = request.getParameter("booktitle");
      String search_author = request.getParameter("author");
      String search_publisher = request.getParameter("publisher");
      String search_deweydecimal = request.getParameter("deweydecimal");
      search(search_booktitle, search_author, search_publisher, search_deweydecimal, response);
   }

   void search(String search_booktitle, String search_author, String search_publisher, 
		   String search_deweydecimal, HttpServletResponse response) throws IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Database Result";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         DBConnection.getDBConnection(getServletContext());
         connection = DBConnection.connection;

         if (search_booktitle.isEmpty() && search_author.isEmpty() && search_publisher.isEmpty()
        		 && search_deweydecimal.isEmpty()) {
            String selectSQL = "SELECT * FROM LibraryTable";
            preparedStatement = connection.prepareStatement(selectSQL);
         } else if (!(search_booktitle.isEmpty())) {
        	String selectSQL = "SELECT * FROM LibraryTable WHERE BOOKTITLE LIKE ?";
	    	String theBooktitle = search_booktitle + "%";
	    	preparedStatement = connection.prepareStatement(selectSQL);
	    	preparedStatement.setString(1, theBooktitle); 
         } else if (!(search_author.isEmpty())) {
        	String selectSQL = "SELECT * FROM LibraryTable WHERE AUTHOR LIKE ?";
        	String theAuthor = search_author + "%";
        	preparedStatement = connection.prepareStatement(selectSQL);
        	preparedStatement.setString(1, theAuthor);
         } else if (!(search_publisher.isEmpty())) {
        	String selectSQL = "SELECT * FROM LibraryTable WHERE PUBLISHER LIKE ?";
        	String thePublisher = search_publisher + "%";
        	preparedStatement = connection.prepareStatement(selectSQL);
        	preparedStatement.setString(1, thePublisher);
         } else if (!(search_deweydecimal.isEmpty())) {
        	String selectSQL = "SELECT * FROM LibraryTable WHERE DEWEYDECIMAL LIKE ?";
        	String theDeweyDecimal = search_deweydecimal + "%";
        	preparedStatement = connection.prepareStatement(selectSQL);
        	preparedStatement.setString(1, theDeweyDecimal);
         }
         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {
            int id = rs.getInt("id");
            String bookTitle = rs.getString("booktitle").trim();
            String author = rs.getString("author").trim();
            String publisher = rs.getString("publisher").trim();
            String deweyDecimal = rs.getString("deweydecimal").trim();
            String checkout = rs.getString("checkout");

            if (search_booktitle.isEmpty() || search_author.isEmpty() || search_publisher.isEmpty() || 
            	search_deweydecimal.isEmpty() || bookTitle.contains(search_booktitle) || 
            	author.contains(search_author) || publisher.contains(search_publisher) ||
            	deweyDecimal.contains(search_deweydecimal)) {
	             out.println("ID: " + id + ", ");
	             out.println("Book Title: " + bookTitle + ", ");
	             out.println("Author: " + author + ", ");
	             out.println("Publisher: " + publisher + ", ");
	             out.println("Dewey Decimal: " + deweyDecimal + ", ");
	             out.println("Checked Out?: " + checkout + "<br>");
	          }
         }
         out.println("<a href=/LibrarySearch/libraryFormSearch.html>Search Data</a> <br>");
         out.println("</body></html>");
         rs.close();
         preparedStatement.close();
         connection.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (preparedStatement != null)
               preparedStatement.close();
         } catch (SQLException se2) {
         }
         try {
            if (connection != null)
               connection.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}