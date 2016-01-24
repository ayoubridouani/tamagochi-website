package controleur;

import java.io.IOException;

import model.*;
import java.util.*;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.*;

// @WebServlet("/gestionUtilisateur")
// @WebServlet(asyncSupported = false, name = "GestionUtilisateur", urlPatterns = {"/gestionUtilisateur"})

@WebServlet("/gestionUtilisateur")
public class GestionUtilisateur extends HttpServlet {

      private static final long serialVersionUID = 1L;
      @EJB
      FacadeJoueurs f;

      public static final int COOKIE_MAX_AGE = 60*60*24*365;
      public static final String CHAMP_MEMOIRE = "remember-me";

      public GestionUtilisateur() {
	    super();
      }

      protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession(true);	

	    if (request.getParameter("a").equals("inscription")) {
		  String nom = request.getParameter("nom");
		  String prenom = request.getParameter("prenom");
		  String email = request.getParameter("email");
		  String mdp = request.getParameter("mdp");
		  f.ajoutJoueur(nom,prenom,email,mdp); //retourne le joueur cree
		  session.setAttribute("usersurname", nom) ;
		  session.setAttribute("username", prenom) ;
		  session.setAttribute("mail", email) ;


		  //Collection<Joueur> listesJoueurs = f.getJoueurs();
		  //request.setAttribute("joueurs",listesJoueurs);
		  request.getRequestDispatcher("jsp/choixTama.jsp").forward(request, response);

	    } else if (request.getParameter("a").equals("connection")) {
	    	  String email = request.getParameter("email");
		  String mdp = request.getParameter("mdp");
		  if (f.hasEmail(email)) {
		  	Joueur j = f.getJoueur(email);
		  	if ( mdp.equals(j.getMdp()) ) {
		  		session.setAttribute("usersurname", j.getNom()) ;
				session.setAttribute("username", j.getPrenom()) ;
		  		session.setAttribute("mail", j.getEmail()) ;
				if (request.getParameter("memoire") != null) {
					setCookie( response, "souvenirEmail", email, COOKIE_MAX_AGE);
					setCookie( response, "souvenirMdp", mdp, COOKIE_MAX_AGE);
				} else {
					setCookie( response, "souvenirEmail", "", 0);
					setCookie( response, "souvenirMdp", "", 0);
				}
				//request.setAttribute("mail", email);
				//request.setAttribute("mdp", mdp);
				request.getRequestDispatcher("jsp/index2.jsp").forward(request, response);
		  	} else {
		  		session.setAttribute("erreur", "Erreur de saisie de l'email ou du mot de passe.");
				session.setAttribute("alert-type", "alert-warning");
				request.getRequestDispatcher("jsp/page_connection.jsp").forward(request, response);
			}
		} else {
			session.setAttribute("erreur", "Erreur de saisie de l'email ou du mot de passe.");
			session.setAttribute("alert-type", "alert-warning");
			request.getRequestDispatcher("jsp/page_connection.jsp").forward(request, response);
		}
      	    } /*else if (request.getParameter("a").equals("warning")) {
		  request.getRequestDispatcher("jsp/page_connection.jsp").forward(request, response);
	    }*/
	    
      /*if (request.getParameter("a").equals("profil")) {
      	request.setAttribute("prenom", session.getAttribute("username"));
      	request.setAttribute("mail", session.getAttribute("mail"));
          request.getRequestDispatcher("jsp/profil.jsp").forward(request, response);
          
	    }*/

        //modifierJoueur
        // Joueur j = (Joueur)session.getAttribute("user");
        // j.password = "kebab"
        //     f.set(j);
      // dans set, on fait un persist
      }

      protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // if (request.getParameter("a").equals("enregistrer")) {
	    // 	request.setAttribute("alert","Inscription reussie");
	    // 	request.setAttribute("class-alert","alert-success");
	    // 	request.getRequestDispatcher("jsp/index.jsp").forward(request,response);		
	    // }
	    // response.setContentType("text/html");
	    // request.getRequestDispatcher("jsp/index.jsp").forward(request, response);

	    doGet(request,response);
      }


       /*
	* Méthode utilitaire gérant la création d'un cookie et son ajout à la
	* réponse HTTP.
	*/
	private static void setCookie( HttpServletResponse response, String nom, String valeur, int maxAge ) {
		Cookie cookie = new Cookie( nom, valeur );
		cookie.setMaxAge( maxAge );
		response.addCookie( cookie );
	}
}
