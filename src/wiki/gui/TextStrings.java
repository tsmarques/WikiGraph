package wiki.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;

import wiki.util.Graph;

public class TextStrings {
	public final static int PT = 0;
	public final static int EN = 1;
	private ArrayList<JMenu> menu;
	private ArrayList<JMenuItem> menuItems;
	private ArrayList<JRadioButtonMenuItem> radioItem;
	private ArrayList<JToggleButton> toggleButtons;
	private ArrayList<JButton> buttons;
	private ArrayList<JLabel> labels;
	private int language;
	
	public TextStrings() {
		language = PT;
		menu = new ArrayList<>();
		menuItems = new ArrayList<>();
		radioItem = new ArrayList<>();
		toggleButtons = new ArrayList<>();
		buttons = new ArrayList<>();
		labels = new ArrayList<>();
	}
	
	public void setLanguage(int language) {
		this.language = language;
		setComponentsText();
	}
	
	public int getCurrentLanguage() {
		return language;
	}
	
	public void addMenu(JMenu jmenu) {
		menu.add(jmenu);
	}
	
	public void addMenuItem(JMenuItem jmenu) {
		menuItems.add(jmenu);
	}
	
	public void addRadioButtonMenu(JRadioButtonMenuItem rButton) {
		radioItem.add(rButton);
	}
	
	public void addToggleButton(JToggleButton jToggle) {
		toggleButtons.add(jToggle);
	}
	
	public void addButton(JButton jButton) {
		buttons.add(jButton);
	}
	
	public void addLabel(JLabel jLabel) {
		labels.add(jLabel);
	}
	
	public void setComponentsText() {
		for(JMenu cursor : menu)
			cursor.setText(getText(cursor.getName()));
		for(JMenuItem cursor : menuItems)
			cursor.setText(getText(cursor.getName()));
		
		for(JRadioButtonMenuItem cursor : radioItem)
			cursor.setText(getText(cursor.getName()));
		
		for(JToggleButton cursor : toggleButtons)
			cursor.setToolTipText(getText(cursor.getName()));
		
		for(JButton cursor : buttons) {
			if(cursor.getName().equals("BT_CHANGE")) {
				if(language == EN)
					cursor.setPreferredSize(new Dimension(90, 25));
				else if(language == PT)
					cursor.setPreferredSize(new Dimension(79, 25));
			}
			else if(cursor.getName().equals("TOOL_TIP_ALL_PAIRS"))
				cursor.setToolTipText(getText("TOOL_TIP_ALL_PAIRS"));
			cursor.setText(getText(cursor.getName()));
		}
		
		for(JLabel cursor : labels)
			cursor.setText(getText(cursor.getName()));
	}
	
	public String getText(String label) {
		if(label.equals("MST_PRIM"))
			return "Prim";
		else if(label.equals("MST_KRUSKAL"))
			return "Kruskal";
		else if(label.equals("SP_DIJKSTRA"))
			return "Dijkstra";
		else if(label.equals("SP_FLOYD_WARSHALL"))
			return "Floyd-Warshall";
		else if(label.equals("SP_BELLMAN_FORD"))
			return "Bellman-Ford";

		
		else if(label.equals("LABEL_MEGABYTES"))
			return "mb";
		else if(label.equals("LABEL_SECONDS"))
			return "s";
		
		else if(label.equals("COPYRIGHTS"))
			return MainWindow.title + " " + MainWindow.version + " " + "(c) 2013, Tiago Sá Marques.\n\n" +
					"If you want to report a bug or just leave a message\ndo it to the email below,\nThank You.\n" +
					"\nup201002596@fc.up.pt";
		
		else if(language == PT) {
			if(label.equals("GRAPH_TAB"))
				return "Grafos";
			else if(label.equals("AI_TAB"))
				return "Inteligência Artificial";
			else if(label.equals("FILE_MENU"))
				return "Ficheiro";
			else if(label.equals("ABOUT_MENU"))
				return "Sobre";
			else if(label.equals("HELP_MENU"))
				return "Ajuda";
			else if(label.equals("HOW_TO_MENU"))
				return "Como fazer";
			else if(label.equals("OTHERS_MENU"))
				return "Outros";
			else if(label.equals("MISC_MENU"))
				return "Vários";
			else if(label.equals("HEURISTIC_SEARCH_MENU"))
				return "Pesquisa Informada";
			else if(label.equals("CON_COMP_MENU"))
				return "Componentes Conexas";
			else if(label.equals("UN_SEARCH_MENU"))
				return "Pesquisa não-informada";
			else if(label.equals("UNIFORM_COST_SEARCH"))
				return " Pesquisa de Custo Uniforme";
			else if(label.equals("LANG_MENU"))
				return "Idioma";
			else if(label.equals("IT_DEEP_SEARCH"))
				return "Pesquisa Iterativa em profundiade";
			else if(label.equals("T_LIMITED_DFS"))
				return "Pesquisa em profundidade Limitada";
			else if(label.equals("MAX_DEPTH"))
				return "Profundidade máxima: ";
			else if(label.equals("LANG_EN"))
				return "Inglês";
			else if(label.equals("LANG_PT"))
				return "Português";
			else if(label.equals("ERROR_LABEL"))
				return "Erro: ";
			else if(label.equals("G_KOSARAJU"))
				return "Algoritmo de Kosaraju";
			else if(label.equals("G_TOP_SORT"))
				return "Ordenação Topológica";
			else if(label.equals("G_IS_DAG"))
				return "É um DAG (Grafo Dirigido Acíclico)";
			else if(label.equals("G_IS_CONNECTED"))
				return "É conexo";
			else if(label.equals("G_REACH_VERT"))
				return "Nós Alcançáveis";
			else if(label.equals("G_TRANSPOSE"))
				return "Grafo transposto";
			
				
			else if(label.equals("BT_NEW"))
				return "Novo grafo";
			else if(label.equals("BT_RESET"))
				return "Reset";
			else if(label.equals("BT_APPLY"))
				return "Aplicar";
			else if(label.equals("BT_ADD_NODE"))
				return "Adicionar nó";
			else if(label.equals("BT_RM_NODE"))
				return "Remover nó";
			else if(label.equals("BT_ADD_EDGE"))
				return "Adicionar aresta";
			else if(label.equals("BT_RM_EDGE"))
				return "Remover aresta";
			else if(label.equals("BT_CHANGE"))
				return "Mudar";
			else if(label.equals("BT_NEW_GRAPH"))
				return "Novo";

			
			else if(label.equals("LABEL_NEW_GRAPH"))
				return "Criar grafo: ";
			else if(label.equals("LABEL_IS_WEIGHTED"))
				return "Com pesos: ";
			else if(label.equals("LABEL_IS_DIRECTED"))
				return "Dirigido: ";
			else if(label.equals("LABEL_TRAVERSES"))
				return "Travessias/Pesquisa";
			
			else if(label.equals("LABEL_MST"))
				return "Árvore de Cobertura Mínima";
			else if(label.equals("LABEL_SHORT_PATH"))
				return "Custo mínimo";
			else if(label.equals("LABEL_OTHERS"))
				return "Outros";
			else if(label.equals("TOOL_TIP_ALL_PAIRS"))
				return "Todos os pares de caminhos mais curtos";
			
			else if(label.equals("LABEL_T_DFS"))
				return "Profunidade (DFS)";
			else if(label.equals("LABEL_T_BFS"))
				return "Largura (BFS)";
			else if(label.equals("LABEL_G_TOP_SORT"))
				return "Ordenação topológica";
			else if(label.equals("LABEL_G_IS_CONNECTED"))
				return "É conexo";
			else if(label.equals("LABEL_G_HAS_CYCLE"))
				return "Tem ciclo(s)";
			else if(label.equals("LABEL_G_IS_DAG"))
				return "É um DAG (Grafo dirigo acíclico)";
			else if(label.equals("SP_ALL_PAIRS"))
				return "Todos os pares";
			
			else if(label.equals("ERROR_GRAPH_FULL"))
				return "Não é possível adicionar mais nós, o grafo está cheio.\nCapacidade máxima = " + Graph.N_MAX;
			else if(label.equals("ERROR_GRAPH_EMPTY"))
				return "O grafo está vazio!";
			else if(label.equals("ERROR_GRAPH_NOT_WEIGHTED"))
				return "Para aplicar este algoritmo o grafo\ntem de ter pesos/valores nas arestas!";
			else if(label.equals("ERROR_GRAPH_DIRECTED"))
				return "O grafo não pode sêr dirigido!";
			else if(label.equals("ERROR_EDGE_EXISTS"))
				return "Essa aresta já existe!";
			else if(label.equals("ERROR_NO_EDGES"))
				return "O grafo não tem arestas!";
			else if(label.equals("ERROR_NEGATIVE_EDGES"))
				return "Para aplicar o algoritmo de Dijkstra o grafo não pode conter\narestas com pesos negativos!";
			else if(label.equals("ERROR_NEGATIVE_CYCLE"))
				return "O algoritmo não pode ser aplicado, pois existe um ciclo negativo";
			
			else if(label.equals("EDGE_WEIGHT_INPUT"))
				return "Peso da Aresta: ";
			else if(label.equals("INFO_SOLUTION"))
				return "Solução: ";
			else if(label.equals("INFO_SELECT_NODE"))
				return "Seleccione o nó para o qual quer saber o caminho mais curto";
			else if(label.equals("INFO_SELECT_NODES"))
				return "Seleccione 2 nós para saber o caminho mais curto entre eles.";
			else if(label.equals("INFO_SELECT_SECOND_NODE"))
				return "Seleccione o 2º nó";
			else if(label.equals("INFO_MIN_SPAN_WEIGHT"))
				return "Custo: ";
			else if(label.equals("INFO_MIN_WEIGHT"))
				return "Custo mínimo: ";
			else if(label.equals("INFO_EXEC_TIME"))
				return "Tempo de execução: ";
			else if(label.equals("INFO_MEM_USED"))
				return "Memória usada: ";
			else if(label.equals("INFO_NO_PATH"))
				return "Não existe um caminho";
			else if(label.equals("INFO_FROM"))
				return " de ";
			else if(label.equals("INFO_TO"))
				return " para ";
			else if(label.equals("G_DIRECTED_CONNECTED"))
				return "O Grafo é conexo";
			else if(label.equals("G_UNDIRECTED_CONNECTED"))
				return "O grafo é conexo.";// Um grafo não dirijido só não é conexo quando existe um nó sem arestas";
			else if(label.equals("G_NOT_CONNECTED"))
				return "O grafo não é conexo. Os nós alcançáveis estão a verde";
		}
		else if(language == EN) {
			if(label.equals("GRAPH_TAB"))
				return "Graphs";
			else if(label.equals("AI_TAB"))
				return "Artificial Intelligence";
			else if(label.equals("FILE_MENU"))
				return "File";
			else if(label.equals("ABOUT_MENU"))
				return "About";
			else if(label.equals("OTHERS_MENU"))
				return "Others";
			else if(label.equals("MISC_MENU"))
				return "Misc";
			else if(label.equals("HEURISTIC_SEARCH_MENU"))
				return "Heuristic Search";
			else if(label.equals("CON_COMP_MENU"))
				return "Connected Components";
			else if(label.equals("HELP_MENU"))
				return "Help";
			else if(label.equals("HOW_TO_MENU"))
				return "How to";
			else if(label.equals("UN_SEARCH_MENU"))
				return "Uninformed Search";
			else if(label.equals("IT_DEEP_SEARCH"))
				return "Iterative Deepening Search";
			else if(label.equals("UNIFORM_COST_SEARCH"))
				return " Uniform Cost Search";
			else if(label.equals("LANG_MENU"))
				return "Language";
			else if(label.equals("LANG_EN"))
				return "English";
			else if(label.equals("LANG_PT"))
				return "Portuguese";
			else if(label.equals("ERROR_LABEL"))
				return "Error: ";
			else if(label.equals("G_KOSARAJU"))
				return "Kosaraju's Algorithm";
			else if(label.equals("G_TOP_SORT"))
				return "Topological Order";
			else if(label.equals("G_IS_DAG"))
				return "Is a DAG (Directed Acyclic Graph)";
			else if(label.equals("G_IS_CONNECTED"))
				return "Is connected";
			else if(label.equals("G_REACH_VERT"))
				return "Reachable nodes";
			else if(label.equals("G_TRANSPOSE"))
				return "Transpose graph";
			else if(label.equals("T_LIMITED_DFS"))
				return "Depth-Limited Search";
			else if(label.equals("MAX_DEPTH"))
				return "Maximum depth: ";
			else if(label.equals("SP_ALL_PAIRS"))
				return "All shortest paths";
			
			else if(label.equals("BT_NEW"))
				return "New graph";
			else if(label.equals("BT_RESET"))
				return "Reset";
			else if(label.equals("BT_APPLY"))
				return "Apply";
			else if(label.equals("BT_ADD_NODE"))
				return "Add node";
			else if(label.equals("BT_RM_NODE"))
				return "Remove node";
			else if(label.equals("BT_ADD_EDGE"))
				return "Add edge";
			else if(label.equals("BT_RM_EDGE"))
				return "Remove edge";
			else if(label.equals("BT_NEW_GRAPH"))
				return "New";
			else if(label.equals("BT_CHANGE"))
				return "Change";
			
			else if(label.equals("LABEL_NEW_GRAPH"))
				return "New Graph: ";
			else if(label.equals("LABEL_IS_WEIGHTED"))
				return "Is weighted: ";
			else if(label.equals("LABEL_IS_DIRECTED"))
				return "Is directed: ";
			else if(label.equals("LABEL_TRAVERSES"))
				return "Traverses/Search";
			else if(label.equals("LABEL_MST"))
				return "Minimmum spanning tree";
			else if(label.equals("LABEL_SHORT_PATH"))
				return "Minimmum cost";
			else if(label.equals("LABEL_OTHERS"))
				return "Others";
			else if(label.equals("TOOL_TIP_ALL_PAIRS"))
				return "All pairs of shortest paths";
			
			
			else if(label.equals("LABEL_T_DFS"))
				return "Depth First Search";
			else if(label.equals("LABEL_T_BFS"))
				return "Breadth First Search";
			else if(label.equals("LABEL_G_TOP_SORT"))
				return "Topological Sort";
			else if(label.equals("LABEL_G_IS_CONNECTED"))
				return "Is Connected";
			else if(label.equals("LABEL_G_HAS_CYCLE"))
				return "Has cycle(s)";
			else if(label.equals("LABEL_G_IS_DAG"))
				return "Is a Directed Acyclic Graph (DAG)";
			
			else if(label.equals("ERROR_GRAPH_FULL"))
				return "Can not add anymore nodes, this graph is full.\nMaximum capacity = " + Graph.N_MAX;
			else if(label.equals("ERROR_GRAPH_EMPTY"))
				return "The graph is empty.";
			else if(label.equals("ERROR_GRAPH_NOT_WEIGHTED"))
				return "To apply this algorithm the graph\nneeds to be weighted!";
			else if(label.equals("ERROR_GRAPH_DIRECTED"))
				return "The graph can't be directed!";
			else if(label.equals("ERROR_EDGE_EXISTS"))
				return "That edge already exists";
			else if(label.equals("ERROR_NO_EDGES"))
				return "This graph has no edges!";
			else if(label.equals("ERROR_NEGATIVE_EDGES"))
				return "To apply this algorithm the graph can't have\nnegative edges, use Bellman-Ford's instead.";
			else if(label.equals("ERROR_NEGATIVE_CYCLE"))
				return "The algorithm can't be applied because there is a negative cycle";
			
			else if(label.equals("EDGE_WEIGHT_INPUT"))
				return "Edge weight: ";
			else if(label.equals("INFO_SOLUTION"))
				return "Solution: ";
			else if(label.equals("INFO_SELECT_NODE"))
				return "Select the node to where you wish to find the shortest path to!";
			else if(label.equals("INFO_SELECT_NODES"))
				return "Select 2 nodes to know the shortest path between them.";
			else if(label.equals("INFO_SELECT_SECOND_NODE"))
				return "Select the 2nd node";
			else if(label.equals("INFO_MIN_SPAN_WEIGHT"))
				return "Cost: ";
			else if(label.equals("INFO_MIN_WEIGHT"))
				return "Minimum cost: ";
			else if(label.equals("INFO_EXEC_TIME"))
				return "Execution time: ";
			else if(label.equals("INFO_MEM_USED"))
				return "Used memory: ";
			else if(label.equals("INFO_NO_PATH"))
				return "There's not path";
			else if(label.equals("INFO_FROM"))
				return " from ";
			else if(label.equals("INFO_TO"))
				return " to ";
			else if(label.equals("G_DIRECTED_CONNECTED"))
				return "The graph is connected";
			else if(label.equals("G_UNDIRECTED_CONNECTED"))
				return "The Graph is connected.";//An undirected graph is not connected only when there is node without edges";
			else if(label.equals("G_NOT_CONNECTED"))
				return "The graph isn't connected.The reachable nodes are in green";
			
		}
		return null;
	}
}
