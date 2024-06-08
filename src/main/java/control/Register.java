import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Hash della password utilizzando bcrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Salva la password hashata nel database
        saveUserToDatabase(username, hashedPassword);
    }

    private void saveUserToDatabase(String username, String hashedPassword) {
        // Implementa la logica per salvare l'utente nel database
    }
}
