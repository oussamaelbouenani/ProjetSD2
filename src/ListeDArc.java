import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ListeDArc extends Graph {
	
	private ArrayList<Route> routes;

	public ListeDArc() {
		super();
		routes = new ArrayList<>();
	}

	@Override
	// Complexit�: ?
	protected void ajouterSommet(Country a) {
		//� compl�ter

	}

	@Override
	// Complexit�: ?
	protected void ajouterArc(Route f) {
		//� compl�ter
		routes.add(f);
	}

	@Override
	// Complexit�: ?
	public Set<Route> arcsSortants(Country a) {
		//� compl�ter
		Set<Route> aRetourner = new HashSet<>();
		for (Route f:
			 routes) {
			if (a.equals(f.getSource()))
				aRetourner.add(f);
		}
		return aRetourner;
	}

	@Override
	// Complexit�: ?
	public boolean sontAdjacents(Country a1, Country a2) {
		// � compl�ter
		for (Route f:
			 routes) {
			if (a1.equals(f.getSource()) && a2.equals(f.getDestination())){
				return true;
			}
		}
		return false;
	}

}
