import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("j_email");
        String password = request.getParameter("j_password");

        // Recupera la password hashata dal database
        String hashedPasswordFromDB = getHashedPasswordFromDatabase(email);

        // Confronta la password fornita dall'utente con quella hashata nel database
        if (BCrypt.checkpw(password, hashedPasswordFromDB)) {
            // Login riuscito
            // Aggiungi qui la logica per gestire il login riuscito
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            // Login fallito
            // Aggiungi qui la logica per gestire il login fallito
            request.getSession().setAttribute("login-error", true);
            response.sendRedirect(request.getContextPath() + "/loginPage.jsp");
        }
    }

    private String getHashedPasswordFromDatabase(String email) {
        // Implementa la logica per recuperare la password hashata dal database
        // Sostituisci con la logica reale per recuperare la password hashata
        // associata all'email specificata dal database
        return "hashedPasswordFromDB"; // Sostituisci con la password hashata effettiva
    }
}

