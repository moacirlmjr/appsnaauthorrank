package br.com.ufpb.appsnaauthorrank.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.partition.api.Part;
import org.gephi.partition.api.Partition;
import org.gephi.partition.api.PartitionController;
import org.gephi.plugins.layout.noverlap.NoverlapLayout;
import org.gephi.plugins.layout.noverlap.NoverlapLayoutBuilder;
import org.gephi.project.api.ProjectController;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.statistics.plugin.GraphDistance;
import org.gephi.statistics.plugin.Modularity;
import org.gephi.statistics.plugin.PageRank;
import org.gephi.statistics.plugin.builder.DegreeBuilder;
import org.gephi.statistics.plugin.builder.ModularityBuilder;
import org.gephi.statistics.plugin.builder.PageRankBuilder;
import org.gephi.statistics.spi.Statistics;
import org.openide.util.Lookup;

import br.com.ufpb.appsnaauthorrank.beans.Artigo;

public class NetworkUtil {

	public static void gerarRedePublicacoes(List<Artigo> artigos)
			throws Exception, IOException {
		// Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(
				ProjectController.class);
		pc.newProject();

		GraphModel graphModel = Lookup.getDefault()
				.lookup(GraphController.class).getModel();
		System.out.println("Iniciando Criação da Rede");
		System.out.println("Criando Nós");
		for (Artigo artigo : artigos) {
			String idNode = artigo.getTitulo();

			Node n0 = graphModel.factory().newNode(idNode + "");
			n0.getNodeData().setLabel(idNode);
			n0.getAttributes().setValue(
					"year",
					artigo.getPubYear() != null ? artigo.getPubYear()
							.replaceAll(" ", "") : "0");
			n0.getAttributes().setValue("journal", artigo.getOndePub());
			n0.getAttributes().setValue(
					"keywords",
					artigo.getKeywords() == null
							|| artigo.getKeywords().equals("") ? "No Keyword"
							: artigo.getKeywords());
			n0.getAttributes().setValue(
					"authors",
					artigo.getAutores() != null ? artigo.getAutores()
							.toString() : "");

			Graph graph = graphModel.getDirectedGraph();

			boolean teste1 = true;
			for (Node n : graph.getNodes().toArray()) {
				if (((String) n.getAttributes().getValue("id"))
						.equals(((String) n0.getAttributes().getValue("id")))) {
					n0 = n;
					teste1 = false;
					break;
				}
			}

			if (teste1) {
				graph.addNode(n0);
			}
		}

		System.out.println("Criando Arestas");
		Graph graph = graphModel.getDirectedGraph();
		for (Artigo artigo : artigos) {
			Node nodeArtigo = null;
			for (Node n : graph.getNodes().toArray()) {
				if (((String) n.getAttributes().getValue("label"))
						.equals(artigo.getTitulo())) {
					nodeArtigo = n;
					break;
				}
			}
			if (artigo.getReferencia() != null) {
				for (Artigo referencia : artigo.getReferencia()) {
					Node nodeReferencia = null;
					for (Node n : graph.getNodes().toArray()) {
						if (((String) n.getAttributes().getValue("label"))
								.equals(referencia.getTitulo())) {
							nodeReferencia = n;
							break;
						}
					}

					if (nodeArtigo != null && nodeReferencia != null) {
						Edge e1 = graphModel.factory().newEdge(nodeArtigo,
								nodeReferencia);
						graph.addEdge(e1);
					}
				}
			}

		}

		System.out
				.println("Quantidade de Nós da Rede: " + graph.getNodeCount());
		System.out.println("Quantidade de Arestas da Rede: "
				+ graph.getEdgeCount());
		System.out
				.println("Calculando Métricas: Centralidade de Grau, PageRank, Modularidade, Betweenness");
		// Export full graph
		AttributeModel attributeModel = Lookup.getDefault()
				.lookup(AttributeController.class).getModel();

		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		distance.execute(graphModel, attributeModel);

		Statistics statistics = null;
		DegreeBuilder dBuilder = new DegreeBuilder();
		statistics = dBuilder.getStatistics();
		statistics.execute(graphModel, attributeModel);

		PageRankBuilder pgb = new PageRankBuilder();
		statistics = pgb.getStatistics();
		statistics.execute(graphModel, attributeModel);

		ModularityBuilder mdb = new ModularityBuilder();
		statistics = mdb.getStatistics();
		statistics.execute(graphModel, attributeModel);

		System.out.println("Realizando Ranking da Rede por PageRank");
		RankingController rankingController = Lookup.getDefault().lookup(
				RankingController.class);
		AttributeColumn column = null;

		column = attributeModel.getNodeTable().getColumn(PageRank.PAGERANK);

		AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController
				.getModel().getTransformer(Ranking.NODE_ELEMENT,
						Transformer.RENDERABLE_SIZE);
		sizeTransformer.setMinSize(1);
		sizeTransformer.setMaxSize(1000);

		Ranking ranking = rankingController.getModel().getRanking(
				Ranking.NODE_ELEMENT, column.getId());
		rankingController.transform(ranking, sizeTransformer);

		// System.out.println("Particionando a Rede por Modularidade");
		column = attributeModel.getNodeTable().getColumn(
				Modularity.MODULARITY_CLASS);

		PartitionController partitionController = Lookup.getDefault().lookup(
				PartitionController.class);
		Partition p = partitionController.buildPartition(column, graph);
		// NodeColorTransformer nodeColorTransformer = new
		// NodeColorTransformer();
		// nodeColorTransformer.randomizeColors(p);
		// partitionController.transform(p, nodeColorTransformer);
		//
		System.out.println("Quantidade de Comunidades Identificadas: "
				+ p.getElementsCount());

		System.out.println("Classificando cada comunidade pelas keywords");

		for (Part pa : p.getParts()) {
			Map<String, Integer> qteKeywordsComunidades = new HashMap<>();
			for (Object obj : pa.getObjects()) {
				Node node = (Node) obj;
				String keywords = (String) node.getAttributes().getValue(
						"keywords");
				String keywordsArray[] = keywords.split(",");
				for (String keyword : keywordsArray) {
					if (!qteKeywordsComunidades.containsKey(keyword)) {
						qteKeywordsComunidades.put(keyword, 1);
					} else {
						qteKeywordsComunidades.put(keyword,
								1 + qteKeywordsComunidades.get(keyword));
					}
				}
			}

			System.out.println("keywords da Comunidade " + pa.getDisplayName()
					+ ": ");
			for (String key : qteKeywordsComunidades.keySet()) {
				System.out.println(key + " " + qteKeywordsComunidades.get(key));
			}
		}

		System.out
				.println("## Calculando Evolução de publicações ao longo dos anos para cada comunidade ##");
		for (Part pa : p.getParts()) {
			Map<String, Integer> anoMap = new HashMap<String, Integer>();
			for (Object obj : pa.getObjects()) {
				Node node = (Node) obj;
				String year = (String) node.getAttributes().getValue(
						"year");
				if (!anoMap.containsKey(year)) {
					anoMap.put(year, 1);
				} else {
					anoMap.put(year, 1 + anoMap.get(year));
				}
			}
			
			System.out.println("Comunidade " + pa.getDisplayName()+ ": " + anoMap);
		}

		System.out.println("Distribuindo a Rede por YifanHU e Noverlap");
		YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();
		layout.setOptimalDistance(100f);

		layout.initAlgo();
		for (int i = 0; i < 100 && layout.canAlgo(); i++) {
			layout.goAlgo();
		}
		layout.endAlgo();

		NoverlapLayout layout2 = new NoverlapLayout(new NoverlapLayoutBuilder());
		layout2.setGraphModel(graphModel);
		layout2.resetPropertiesValues();

		layout2.initAlgo();
		for (int i = 0; i < 100 && layout2.canAlgo(); i++) {
			layout2.goAlgo();
		}
		layout2.endAlgo();

		System.out.println("Gerando GEXF");
		// Export to GRaphml
		ExportController ec = Lookup.getDefault()
				.lookup(ExportController.class);
		ec.exportFile(new File(
				"C:\\Users\\Moacir\\Desktop\\GrafoPaperCrawler.gexf"));

	}
}
