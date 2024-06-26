package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.CartBean;
import model.CartModel;
import model.PreferitiModel;
import model.ProductBean;
import model.ProductModel;

@WebServlet("/ProductControl")
public class ProductControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    static ProductModel model;
    
    static {
        model = new ProductModel();
    }
    
    public ProductControl() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configurare Content Security Policy
        response.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self'; style-src 'self';");

        try {
            if (request.getParameter("action") != null && request.getParameter("action").compareTo("dettaglio") == 0) {
                String codiceStr = request.getParameter("codice");
                if (codiceStr == null || codiceStr.isEmpty()) {
                    throw new IllegalArgumentException("Codice prodotto non fornito");
                }
                int codice = Integer.parseInt(codiceStr);

                ProductBean prodotto = model.doRetrieveByKey(codice);
                request.setAttribute("prodottoDettaglio", prodotto);

                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/productDetail.jsp");
                dispatcher.forward(request, response);

            } else if (request.getParameter("action") != null && request.getParameter("action").compareTo("elimina") == 0) {
                @SuppressWarnings("unchecked")
                Collection<ProductBean> lista = (Collection<ProductBean>) request.getSession().getAttribute("products");
                int codice = Integer.parseInt(request.getParameter("codice"));
                Collection<ProductBean> collezione = model.deleteProduct(codice, lista);

                request.getSession().removeAttribute("products");
                request.getSession().setAttribute("products", collezione);
                request.getSession().setAttribute("refreshProduct", true);

                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ProductsPage.jsp");
                dispatcher.forward(request, response);

            } else if (request.getParameter("action") != null && request.getParameter("action").compareTo("modificaForm") == 0) {
                ProductBean bean = model.doRetrieveByKey(Integer.parseInt(request.getParameter("codice")));
                request.setAttribute("updateProd", bean);

                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/modifica-prodotto.jsp");
                dispatcher.forward(request, response);

            } else if (request.getParameter("action") != null && request.getParameter("action").compareTo("modifica") == 0) {
                ProductBean bean = new ProductBean();
                bean.setCodice(Integer.parseInt(request.getParameter("codice")));
                bean.setNome(request.getParameter("nome"));
                bean.setDescrizione(request.getParameter("descrizione"));
                bean.setPrezzo(Double.parseDouble(request.getParameter("prezzo")));
                bean.setSpedizione(Double.parseDouble(request.getParameter("spedizione")));
                bean.setTag(request.getParameter("tag"));
                bean.setTipologia(request.getParameter("tipologia"));

                model.updateProduct(bean);

                if (request.getSession().getAttribute("carrello") != null) {
                    CartModel cartmodel = new CartModel();
                    CartBean newCart = cartmodel.updateCarrello(bean, (CartBean) request.getSession().getAttribute("carrello"));
                    request.getSession().setAttribute("carrello", newCart);
                }

                if (request.getSession().getAttribute("preferiti") != null) {
                    PreferitiModel preferitiModel = new PreferitiModel();
                    @SuppressWarnings("unchecked")
                    Collection<ProductBean> lista = preferitiModel.updatePreferiti(bean, (Collection<ProductBean>) request.getSession().getAttribute("preferiti"));
                    request.getSession().setAttribute("preferiti", lista);
                }

                request.getSession().setAttribute("refreshProduct", true);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);

            } else {
                String tipologia = (String) request.getSession().getAttribute("tipologia");

                request.removeAttribute("products");
                request.setAttribute("products", model.doRetrieveAll(tipologia));

                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ProductsPage.jsp?tipologia=" + tipologia);
                dispatcher.forward(request, response);
            }
        } catch (IllegalArgumentException e) {
            log("Errore nei parametri di input", e);
            request.setAttribute("errorMessage", "Parametri di input non validi.");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error500.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            log("Errore nel database", e);
            request.setAttribute("errorMessage", "Errore interno del server. Si prega di riprovare più tardi.");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error500.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            log("Errore generico", e);
            request.setAttribute("errorMessage", "Si è verificato un errore interno. Si prega di riprovare più tardi.");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error500.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

