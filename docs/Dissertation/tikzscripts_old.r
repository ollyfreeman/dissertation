require(tikzDevice)

SubFigureAxisScaling <- 0.75

percolation <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/percolation.tex', width =7, height=5)

	source("/Users/olly_freeman/Documents/Part2/Dissertation/scripts.r")
	plotValidPathsFixedClusteringMultiple()

	dev.off()
}

boxPlotDistanceAll <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/boxPlotDistanceAll.tex', width = 7, height=5)

	source("/Users/olly_freeman/Dropbox/Part2Project/scripts.r")
	boxPlotParameter("Distance","All","Algorithm", SubFigureAxisScaling)

	dev.off()
}

boxPlotDistanceAny <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/boxPlotDistanceAny.tex', width =3.5, height=4)

	source("/Users/olly_freeman/Dropbox/Part2Project/scripts.r")
	boxPlotParameter("Distance","Any","Algorithm",SubFigureAxisScaling)

	dev.off()
}

classicEquivalence <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/classicEquivalence.tex', width = 2.5, height=4)

	source("/Users/olly_freeman/Dropbox/Part2Project/scripts.r")
	colourParameter("Distance", "Classic", "Algorithm","doesntmatter","true", SubFigureAxisScaling)

	dev.off()
}

boxPlotAngleAll <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/boxPlotAngleAll.tex', width =7, height=5)

	source("/Users/olly_freeman/Dropbox/Part2Project/scripts.r")
	boxPlotParameter("Angle","All","Algorithm", SubFigureAxisScaling)

	dev.off()
}

boxPlotAngleAny <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/boxPlotAngleAny.tex', width =3.5, height=4)

	source("/Users/olly_freeman/Dropbox/Part2Project/scripts.r")
	boxPlotParameter("Angle","Any","Algorithm", SubFigureAxisScaling)

	dev.off()
}

colourDistanceLDDB <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/colourDistanceLDDB.tex', width =3, height=4)

	source("/Users/olly_freeman/Dropbox/Part2Project/scripts.r")
	colourParameter("Distance","doesntmatter","LDDB", "doesntmatter","true", SubFigureAxisScaling)

	dev.off()
}

colourNodesExpandedNoBAS <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/colourNodesExpandedNoBAS.tex', width =3.5, height=4)

	source("/Users/olly_freeman/Dropbox/Part2Project/scripts.r")
	colourParameter("NodesExpanded","NoBAS","Algorithm", "Nodes","true", SubFigureAxisScaling)

	dev.off()
}

colourNodesExpandedThree_blocks <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/colourNodesExpandedThree_blocks.tex', width =3, height=4)

	source("/Users/olly_freeman/Dropbox/Part2Project/scripts.r")
	colourParameter("NodesExpanded","Three","Algorithm", "Blocks","true", SubFigureAxisScaling)

	dev.off()
}

colourBASBlocksExpanded <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/colourBASBlocksExpanded.tex', width =3, height=4)

	source("/Users/olly_freeman/Dropbox/Part2Project/scripts.r")
	colourBASNodesExpanded("Blocks","true", SubFigureAxisScaling)

	dev.off()
}

colourNodesExpandedThree_nodes <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/colourNodesExpandedThree_nodes.tex', width =2.25, height=4)

	source("/Users/olly_freeman/Dropbox/Part2Project/scripts.r")
	colourParameter("NodesExpanded","Three","Algorithm", "Nodes","true", SubFigureAxisScaling)

	dev.off()
}

colourNodesExpandedThree_neighbours <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/colourNodesExpandedThree_neighbours.tex', width =2.25, height=4)

	source("/Users/olly_freeman/Dropbox/Part2Project/scripts.r")
	colourParameter("NodesExpanded","Three","Algorithm", "Nodes","true", SubFigureAxisScaling)

	dev.off()
}

colourBASNeighboursExpanded <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/colourBASNeighboursExpanded.tex', width =2.25, height=4)

	source("/Users/olly_freeman/Dropbox/Part2Project/scripts.r")
	colourBASNodesExpanded("Neighbours","true", SubFigureAxisScaling)

	dev.off()
}

boxPlotTimeAll <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/boxPlotTimeAll.tex', width =7, height=5)

	source("/Users/olly_freeman/Dropbox/Part2Project/scripts.r")
	boxPlotParameter("AlgorithmTime", "All", "Algorithm", SubFigureAxisScaling)


	dev.off()
}

timeToExpansionRatio1 <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/timeToExpansionRatio.tex', width =7, height=5)

	source("/Users/olly_freeman/Dropbox/Part2Project/scripts.r")
	timeToExpansionRatio(SubFigureAxisScaling)


	dev.off()
}

final <- function() {
	tikz( '/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/Figures/nfigfns1.tex', width =7, height=5)

	source("/Users/olly_freeman/Dropbox/Part2Project/docs/Dissertation/Figures/nfigfns.r")
	nfig42()


	dev.off()
}

#percolation()
#boxPlotDistanceAll()
#boxPlotDistanceAny()
#classicEquivalence()
#boxPlotAngleAll()
#boxPlotAngleAny()
#colourDistanceLDDB()
#colourNodesExpandedNoBAS()
#colourNodesExpandedThree_blocks()		
#colourBASBlocksExpanded()
#colourNodesExpandedThree_nodes()	
#colourNodesExpandedThree_neighbours()
#colourBASNeighboursExpanded()
#boxPlotTimeAll()
#timeToExpansionRatio1()
#final()	