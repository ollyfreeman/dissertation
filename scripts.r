require(ggplot2)
require(reshape2)

DijkstraLabel = "{\\em Dijkstra}"
AStarLabel = "{\\em A*}"
AStarSmoothedLabel = "{\\em A* wps}"
ThetaStarLabel = "{\\em Theta*}"
LazyThetaStarLabel = "{\\em Lazy Theta*}"
BlockAStarLabel = "{\\em Block A*}"

boxPlotParameter<-function(Parameter,ClassicAnyAllNoBASThree, AlgorithmOrLDDB, SubFigureAxisScaling) {
	df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")

	AStarVis <- (subset(df,Algorithm=="AStarVisibility"))[,Parameter]
	Dijkstra  <- (subset(df,Algorithm=="Dijkstra"))[,Parameter]
	AStar  <- (subset(df,Algorithm=="AStar"))[,Parameter]
	AStarSmoothed  <- (subset(df,Algorithm=="AStarSmoothed"))[,Parameter]
	ThetaStar  <- (subset(df,Algorithm=="ThetaStar"))[,Parameter]
	LazyThetaStar  <- (subset(df,Algorithm=="LazyThetaStar"))[,Parameter]
	BlockAStar  <- (subset(df,Algorithm=="BlockAStar"))[,Parameter]

	b2 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar2N.csv")
	b3 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar3N.csv")
	b4 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar4N.csv")
	b2 <- b2[,Parameter]
	b3 <- b3[,Parameter]
	b4 <- b4[,Parameter]
	
	Outline <- TRUE
	if(Parameter == "Distance") {
		ylabel <- "Path length/Optimal path length"

		Dijkstra <- Dijkstra/AStarVis
		AStar  <- AStar/AStarVis
		AStarSmoothed <- AStarSmoothed/AStarVis
		ThetaStar  <- ThetaStar/AStarVis
		LazyThetaStar  <- LazyThetaStar/AStarVis
		BlockAStar  <- BlockAStar/AStarVis
	} else if (Parameter == "Angle") {
		ylabel <- "Path angle-sum/Optimal path angle-sum"
		Dijkstra <- Dijkstra/AStarVis
		AStar  <- AStar/AStarVis
		AStarSmoothed <- AStarSmoothed/AStarVis
		ThetaStar  <- ThetaStar/AStarVis
		LazyThetaStar  <- LazyThetaStar/AStarVis
		BlockAStar  <- BlockAStar/AStarVis
	} else if (Parameter == "AlgorithmTime") {
		ylabel <- "Execution Time/ms"
		Outline <- FALSE
	} else {
		ylabel <- "Expansions $\\times 10^{3}$"

		Dijkstra <- Dijkstra/1000
		AStar  <- AStar/1000
		AStarSmoothed <- AStarSmoothed/1000
		ThetaStar  <- ThetaStar/1000
		LazyThetaStar  <- LazyThetaStar/1000
		BlockAStar  <- BlockAStar/1000
	}
	if(AlgorithmOrLDDB == "Algorithm") {
		xlabel <- ""
		if(ClassicAnyAllNoBASThree == "Classic") {
			plot1 <- cbind(Dijkstra,AStar)
			par(mar=c(2.5,4,1,1))
			boxplot(plot1, outline=Outline, las=1, xaxt="n", cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling,col="grey", outcol="black", outpch=19,outcex=0.5, ann=FALSE)
			mtext(ylabel,side=2,line=3,cex= SubFigureAxisScaling)
			axis(1, at=1:2, labels=c(DijkstraLabel, AStarLabel), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		} else if (ClassicAnyAllNoBASThree == "Any") {
			plot1 <- cbind(AStarSmoothed,ThetaStar,LazyThetaStar,BlockAStar)
			par(mar=c(2.5,4,1,1))
			boxplot(plot1, outline= Outline, las=1, xaxt="n", cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling,col="grey", outcol="black", outpch=19,outcex=0.5, ann=FALSE)
			mtext(ylabel,side=2,line=3,cex= SubFigureAxisScaling)
			axis(1, at=1:4, labels=c(AStarSmoothedLabel,ThetaStarLabel,"{\\em L. Theta*}", "{\\em Bl. A*}"), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)		
		} else if (ClassicAnyAllNoBASThree == "All") {
			plot1 <- cbind(Dijkstra,AStar,AStarSmoothed,ThetaStar,LazyThetaStar,BlockAStar)
			par(mar=c(2.5,4,1,1))
			boxplot(plot1,outline= Outline, las=1, xaxt="n", cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling,col="grey", outcol="black", outpch=19,outcex=0.5, ann=FALSE)
			mtext(ylabel,side=2,line=3,cex= SubFigureAxisScaling)
			axis(1, at=1:6, labels=c(DijkstraLabel,AStarLabel,AStarSmoothedLabel,ThetaStarLabel,LazyThetaStarLabel, BlockAStarLabel), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		} else if (ClassicAnyAllNoBASThree == "NoBAS"){
			plot1 <- cbind(Dijkstra,AStar,AStarSmoothed,ThetaStar,LazyThetaStar)
			par(mar=c(2.5,4,1,1))
			boxplot(plot1,outline= Outline, las=1, xaxt="n", cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling,col="grey", outcol="black", outpch=19,outcex=0.5, ann=FALSE)
			mtext(ylabel,side=2,line=3,cex= SubFigureAxisScaling)
			axis(1, at=1:5, labels=c(DijkstraLabel,AStarLabel,AStarSmoothedLabel,ThetaStarLabel,LazyThetaStarLabel), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		} else {
			plot1 <- cbind(AStar,ThetaStar, BlockAStar)
			par(mar=c(2.5,4,1,1))
			boxplot(plot1,outline= Outline, las=1, xaxt="n", cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling,col="grey", outcol="black", outpch=19,outcex=0.5, ann=FALSE)
			mtext(ylabel,side=2,line=3,cex= SubFigureAxisScaling)
			axis(1, at=1:3, labels=c(AStarLabel,ThetaStarLabel,BlockAStarLabel), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		}
	} else {
		xlabel <- ""
		plot1 <- cbind(b2,b3,b4)
		boxplot(plot1,outline= Outline, las=1, xaxt="n", cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling,col="grey", outcol="black", outpch=19,outcex=0.5, ann=FALSE)
		mtext(ylabel,side=2,line=3,cex= SubFigureAxisScaling)
		axis(1, at=1:3, labels=c(2,3,4),cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
	}
}
colourParameter <-function(Parameter, ClassicAnyAllNoBASThree, AlgorithmOrLDDB, BlocksOrNodesOrNeighbours, Lines, SubFigureAxisScaling) {
	df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
	
	AStarVis <- (subset(df,Algorithm=="AStarVisibility"))[,Parameter]
	Dijkstra  <- (subset(df,Algorithm=="Dijkstra"))[,Parameter]
	AStar  <- (subset(df,Algorithm=="AStar"))[,Parameter]
	AStarSmoothed  <- (subset(df,Algorithm=="AStarSmoothed"))[,Parameter]
	ThetaStar  <- (subset(df,Algorithm=="ThetaStar"))[,Parameter]
	LazyThetaStar  <- (subset(df,Algorithm=="LazyThetaStar"))[,Parameter]
	BlockAStar  <- (subset(df,Algorithm=="BlockAStar"))[,Parameter]

	b2 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar2N.csv")
	b3 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar3N.csv")
	b4 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar4N.csv")
	b2 <- b2[,Parameter]
	b3 <- b3[,Parameter]
	b4 <- b4[,Parameter]
	b2 <- b2/AStarVis
	b3 <- b3/AStarVis
	b4 <- b4/AStarVis

	b4N <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar4N.csv")
	b4N <- subset(b4N, select = "NodesExpanded")
	b4N <- t(b4N)
	
	if(Parameter == "Distance") {
		ylabel <- "Path length/Optimal path length"
		Dijkstra <- Dijkstra/AStarVis
		AStar  <- AStar/AStarVis
		AStarSmoothed <- AStarSmoothed/AStarVis
		ThetaStar  <- ThetaStar/AStarVis
		LazyThetaStar  <- LazyThetaStar/AStarVis
		BlockAStar  <- BlockAStar/AStarVis
		YParam <- 2.5
	} else if (Parameter == "Angle") {
		ylabel <- "Path angle-sum/degrees"
		YParam <- 2.5
	} else if (Parameter == "AlgorithmTime") {
		ylabel <- "ExecutionTime/ms"
		YParam <- 2.5
	} else {
		ylabel <- "Expansions $\\times 10^{3}$"
		YParam <- 2
		if(BlocksOrNodesOrNeighbours == "Blocks") {
		
			Dijkstra <- Dijkstra/1000
			AStar  <- AStar/1000
			AStarSmoothed <- AStarSmoothed/1000
			ThetaStar  <- ThetaStar/1000
			LazyThetaStar  <- LazyThetaStar/1000
			BlockAStar  <- BlockAStar/1000
		} else if(BlocksOrNodesOrNeighbours == "Nodes") {
		
			Dijkstra <- Dijkstra/1000
			AStar  <- AStar/1000
			AStarSmoothed <- AStarSmoothed/1000
			ThetaStar  <- ThetaStar/1000
			LazyThetaStar  <- LazyThetaStar/1000
			BlockAStar  <- b4N
			BlockAStar <- BlockAStar/1000	
		} else {
			ylabel <- "Neighbour visits $\\times 10^{3}$"
			Dijkstra <- Dijkstra/125
			AStar  <- AStar/125
			AStarSmoothed <- AStarSmoothed/125
			ThetaStar  <- ThetaStar/125
			LazyThetaStar  <- LazyThetaStar/125
			BlockAStar  <- b4N
			BlockAStar <- BlockAStar/62.5
		}
	}
	if(AlgorithmOrLDDB == "Algorithm") {
		xlabel <- "" 
		#xlabel<- "Algorithm"
		if(ClassicAnyAllNoBASThree =="Classic") {
			maximum <- max(max(Dijkstra),max(AStar)) #maximum <- max(Dijkstra)
			minimum <- min(min(Dijkstra), min(AStar))
			scaler <- Dijkstra[1]
			xaxis = c(1,2)
			vector <- c(Dijkstra[1],AStar[1])
			ylimit <- max(max(Dijkstra),max(AStar))
		} else if(ClassicAnyAllNoBASThree == "Any") {
			maximum <- max(max(AStarSmoothed),max(ThetaStar),max(LazyThetaStar), max(BlockAStar)) #maximum <- max(AStarSmoothed)
			minimum <- min(min(AStarSmoothed),min(ThetaStar),min(LazyThetaStar), min(BlockAStar))
			scaler <- AStarSmoothed[1]
			xaxis = c(1,2,3,4)
			vector <- c(AStarSmoothed[1],ThetaStar[1],LazyThetaStar[1], BlockAStar[1])
			ylimit <- max(max(AStarSmoothed),max(ThetaStar),max(LazyThetaStar), max(BlockAStar))
		} else if(ClassicAnyAllNoBASThree == "All") {
			maximum <- max(max(Dijkstra),max(AStar),max(AStarSmoothed),max(ThetaStar),max(LazyThetaStar), max(BlockAStar)) #maximum <- max(Dijkstra)
			minimum <- min(min(Dijkstra), min(AStar), min(AStarSmoothed), min(ThetaStar), min(LazyThetaStar), min(BlockAStar))
			scaler <- Dijkstra[1]
			xaxis = c(1,2,3,4,5,6)
			vector <- c(Dijkstra[1],AStar[1],AStarSmoothed[1],ThetaStar[1],LazyThetaStar[1], BlockAStar[1])
			ylimit <- max(max(Dijkstra),max(AStar),max(AStarSmoothed),max(ThetaStar),max(LazyThetaStar), max(BlockAStar))
		} else if(ClassicAnyAllNoBASThree == "NoBAS") {
			maximum <- max(max(AStar)) #maximum <- max(Dijkstra)
			minimum <- min(min(AStar))
			scaler <- AStar[1]
			xaxis = c(1,2,3,4,5)
			vector <- c(Dijkstra[1],AStar[1],AStarSmoothed[1],ThetaStar[1],LazyThetaStar[1])
			ylimit <- max(max(Dijkstra),max(AStar),max(AStarSmoothed),max(ThetaStar),max(LazyThetaStar))
		} else {
			maximum <- max(max(AStar))
			minimum <- min(min(AStar))
			scaler <- AStar[1]
			xaxis = c(1,2,3)
			vector <- c(AStar[1],ThetaStar[1],BlockAStar[1])
			ylimit <- max(max(AStar),max(ThetaStar),max(BlockAStar))
		}
	} else {
		xlabel<- ""
		#xlabel<- "Block size"
		maximum <- max(b2)
		minimum <- 1
		scaler <- b2[1]
		xaxis = c(2,3,4)
		vector <- c(b2[1],b3[1],b4[1])
		ylimit <- max(max(b2),max(b3),max(b4))
		YParam <- 3
	}
	red <- (maximum-scaler)*(0.999/(maximum-minimum))
	blue <- 1 - (maximum-scaler)*(0.999/(maximum-minimum))
	par(mar=c(2.5,YParam+1,1,1))
	plot(xaxis, vector, col = rgb(red,0.0,blue), pch=4, ylim=c(1, ylimit), xaxt="n", las=1, cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling, ann=FALSE)
	mtext(ylabel,side=2,line=YParam,cex= SubFigureAxisScaling)	
if(AlgorithmOrLDDB == "Algorithm") {
		if(ClassicAnyAllNoBASThree =="Classic") {
			axis(1, at=1:2, labels=c(DijkstraLabel, AStarLabel), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		} else if(ClassicAnyAllNoBASThree =="Any") {
			axis(1, at=1:4, labels=c(AStarSmoothedLabel,ThetaStarLabel,LazyThetaStarLabel, BlockAStarLabel), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		} else if (ClassicAnyAllNoBASThree == "All") {
			axis(1, at=1:6, labels=c(DijkstraLabel,AStarLabel,AStarSmoothedLabel,ThetaStarLabel,LazyThetaStarLabel, BlockAStarLabel), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		} else if (ClassicAnyAllNoBASThree == "NoBAS") {
			axis(1, at=1:5, labels=c(DijkstraLabel,AStarLabel,AStarSmoothedLabel,ThetaStarLabel,LazyThetaStarLabel), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		} else {
			axis(1, at=1:3, labels=c(AStarLabel,ThetaStarLabel,BlockAStarLabel), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		}
	} else {
		axis(1, at=2:4, labels=c(2,3,4), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
	}
	if(Lines) {
		lines(xaxis, vector, col = rgb(red,0.0,blue))
	}
	for(i in 1:length(ThetaStar)) {
		if(AlgorithmOrLDDB == "Algorithm") {
			if(ClassicAnyAllNoBASThree =="Classic") {
				scaler <- Dijkstra[i]
				axis = c(1,2)
				vector <- c(Dijkstra[i],AStar[i])
			} else if (ClassicAnyAllNoBASThree =="Any") {
				scaler <- AStarSmoothed[i]
				axis = c(1,2,3,4)
				vector <- c(AStarSmoothed[i],ThetaStar[i],LazyThetaStar[i], BlockAStar[i])
			} else if (ClassicAnyAllNoBASThree == "All") {
				scaler <- Dijkstra[i]
				axis = c(1,2,3,4,5,6)
				vector <- c(Dijkstra[i],AStar[i],AStarSmoothed[i],ThetaStar[i],LazyThetaStar[i], BlockAStar[i])
			} else if (ClassicAnyAllNoBASThree == "NoBAS") {
				scaler <- AStar[i]
				axis = c(1,2,3,4,5)
				vector <- c(Dijkstra[i],AStar[i],AStarSmoothed[i],ThetaStar[i],LazyThetaStar[i])
			} else {
				scaler <- AStar[i]
				axis = c(1,2,3)
				vector <- c(AStar[i],ThetaStar[i],BlockAStar[i])
			}
		} else {
			scaler <- b2[i]
			axis = c(2,3,4)
			vector <- c(b2[i],b3[i],b4[i])
		}
		red <- (maximum-scaler)*(0.999/(maximum-minimum))
		blue <- 1 - (maximum-scaler)*(0.999/(maximum-minimum))
		points(xaxis, vector, pch=4, col = rgb(red,0.0,blue))
		if(Lines) {
			lines(xaxis, vector, col = rgb(red,0.0,blue))
		}
	}	
}

#to show how number of blocks expanded decreases as block size increases 
colourBASNodesExpanded <-function(BlocksOrNodesOrNeighbours, Lines, SubFigureAxisScaling) {
	if(BlocksOrNodesOrNeighbours == "Blocks") {
		suffix <- "B.csv"
		multiplier <- c(1,1,1)
		ylabel <- "Expansions $\\times 10^{3}$"
	} else if(BlocksOrNodesOrNeighbours == "Nodes"){
		suffix <- "N.csv"
		multiplier <- c(1,1,1)
		ylabel <- "Expansions $\\times 10^{3}$"
	} else {
		suffix <- "N.csv"
		multiplier <- c(8,12,16)
		ylabel <- "Neighbour visits $\\times 10^{3}$"
	}
	b2 <- read.csv(paste("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar2",suffix,sep=""))
	b3 <- read.csv(paste("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar3",suffix,sep=""))
	b4 <- read.csv(paste("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar4",suffix,sep=""))
	b2 <- subset(b2, select = "NodesExpanded")
	b3 <- subset(b3, select = "NodesExpanded")
	b4 <- subset(b4, select = "NodesExpanded")
	b2 <- t(b2)
	b2 <- b2/1000
	b3 <- t(b3)
	b3 <- b3/1000
	b4 <- t(b4)
	b4 <- b4/1000
	maxb2 <- max(b2)
	red <- (maxb2-b2[1])/(0.999 * maxb2)
	blue <- 1 - (maxb2-b2[1])/(0.999* maxb2)

	par(mar=c(2.5,3,1,1))
	plot(c(2,3.5,4), (c(b2[1],b3[1],b4[1])*multiplier), col = rgb(red,0.0,blue), pch=4, ylim=c(0, max(max(b2*multiplier[1]),max(b3*multiplier[2]),max(b4*multiplier[3]))), xaxt="n", las=1, cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling, ann=FALSE)
	mtext(ylabel,side=2,line=2,cex= SubFigureAxisScaling)
	axis(1, at=2:4, labels=c(2,3,4), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
	if(Lines) {
		lines(c(2,3,4), (c(b2[1],b3[1],b4[1])*multiplier), col = rgb(red,0.0,blue))
	}
	for(i in 2:length(b2)) {
		red <- (maxb2-b2[i])/(0.999 * maxb2)
		blue <- 1 - (maxb2-b2[i])/(0.999* maxb2)
		points(c(2,3,4), (c(b2[i],b3[i],b4[i])*multiplier), pch=4, col = rgb(red,0.0,blue), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		if(Lines) {
			lines(c(2,3,4), (c(b2[i],b3[i],b4[i])*multiplier), col = rgb(red,0.0,blue), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		}
	}		
}

LDDBTime <-function() {
	uncompressed2df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/3/200_20_50_blockAStar2uncompressed.csv")
	uncompressed3df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/3/200_20_50_blockAStar3uncompressed.csv")
	uncompressed4df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/3/200_20_50_blockAStar4uncompressed.csv")
	uncompressed2 <- subset(uncompressed2df, select = "AlgorithmTime")
	uncompressed3 <- subset(uncompressed3df, select = "AlgorithmTime")
	uncompressed4 <- subset(uncompressed4df, select = "AlgorithmTime")
	uncompressed2_ <- subset(uncompressed2df, select = "GraphCreationTime")
	uncompressed3_ <- subset(uncompressed3df, select = "GraphCreationTime")
	uncompressed4_ <- subset(uncompressed4df, select = "GraphCreationTime")
	uncompressed2 <- t(uncompressed2)
	uncompressed3 <- t(uncompressed3)
	uncompressed4 <- t(uncompressed4)
	uncompressed2_ <- t(uncompressed2_)
	uncompressed3_ <- t(uncompressed3_)
	uncompressed4_ <- t(uncompressed4_)
	uncompressedMED2 <- median(uncompressed2)
	uncompressedMED3 <- median(uncompressed3)
	uncompressedMED4 <- median(uncompressed4)
	uncompressedMAX2 <- max(max(uncompressed2), max(uncompressed2_))
	uncompressedMAX3 <- max(max(uncompressed3), max(uncompressed3_))
	uncompressedMAX4 <- max(max(uncompressed4), max(uncompressed4_))
	uncompressedMED2_ <- median(uncompressed2_)
	uncompressedMED3_ <- median(uncompressed3_)
	uncompressedMED4_ <- median(uncompressed4_)

	bitwise2df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/3/200_20_50_blockAStar2bitwise.csv")
	bitwise3df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/3/200_20_50_blockAStar3bitwise.csv")
	bitwise4df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/3/200_20_50_blockAStar4bitwise.csv")
	bitwise2 <- subset(bitwise2df, select = "AlgorithmTime")
	bitwise3 <- subset(bitwise3df, select = "AlgorithmTime")
	bitwise4 <- subset(bitwise4df, select = "AlgorithmTime")
	bitwise2_ <- subset(bitwise2df, select = "GraphCreationTime")
	bitwise3_ <- subset(bitwise3df, select = "GraphCreationTime")
	bitwise4_ <- subset(bitwise4df, select = "GraphCreationTime")
	bitwise2 <- t(bitwise2)
	bitwise3 <- t(bitwise3)
	bitwise4 <- t(bitwise4)
	bitwise2_ <- t(bitwise2_)
	bitwise3_ <- t(bitwise3_)
	bitwise4_ <- t(bitwise4_)
	bitwiseMED2 <- median(bitwise2)
	bitwiseMED3 <- median(bitwise3)
	bitwiseMED4 <- median(bitwise4)
	bitwiseMAX2 <- max(max(bitwise2), max(bitwise2_))
	bitwiseMAX3 <- max(max(bitwise3), max(bitwise3_))
	bitwiseMAX4 <- max(max(bitwise4), max(bitwise4_))
	bitwiseMED2_ <- median(bitwise2_)
	bitwiseMED3_ <- median(bitwise3_)
	bitwiseMED4_ <- median(bitwise4_)

	bitwiseMED3_

	geometric2df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/3/200_20_50_blockAStar2geometric.csv")
	geometric3df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/3/200_20_50_blockAStar3geometric.csv")
	geometric4df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/3/200_20_50_blockAStar4geometric.csv")
	geometric2 <- subset(geometric2df, select = "AlgorithmTime")
	geometric3 <- subset(geometric3df, select = "AlgorithmTime")
	geometric4 <- subset(geometric4df, select = "AlgorithmTime")
	geometric2_ <- subset(geometric2df, select = "GraphCreationTime")
	geometric3_ <- subset(geometric3df, select = "GraphCreationTime")
	geometric4_ <- subset(geometric4df, select = "GraphCreationTime")
	geometric2 <- t(geometric2)
	geometric3 <- t(geometric3)
	geometric4 <- t(geometric4)
	geometric2_ <- t(geometric2_)
	geometric3_ <- t(geometric3_)
	geometric4_ <- t(geometric4_)
	geometricMED2 <- median(geometric2)
	geometricMED3 <- median(geometric3)
	geometricMED4 <- median(geometric4)
	geometricMAX2 <- max(max(geometric2), max(geometric2_))
	geometricMAX3 <- max(max(geometric3), max(geometric3_))
	geometricMAX4 <- max(max(geometric4), max(geometric4_))
	geometricMED2_ <- median(geometric2_)
	geometricMED3_ <- median(geometric3_)
	geometricMED4_ <- median(geometric4_)

	geobit2df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/3/200_20_50_blockAStar2geobit.csv")
	geobit3df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/3/200_20_50_blockAStar3geobit.csv")
	geobit4df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/3/200_20_50_blockAStar4geobit.csv")
	geobit2 <- subset(geobit2df, select = "AlgorithmTime")
	geobit3 <- subset(geobit3df, select = "AlgorithmTime")
	geobit4 <- subset(geobit4df, select = "AlgorithmTime")
	geobit2_ <- subset(geobit2df, select = "GraphCreationTime")
	geobit3_ <- subset(geobit3df, select = "GraphCreationTime")
	geobit4_ <- subset(geobit4df, select = "GraphCreationTime")
	geobit2 <- t(geobit2)
	geobit3 <- t(geobit3)
	geobit4 <- t(geobit4)
	geobit2_ <- t(geobit2_)
	geobit3_ <- t(geobit3_)
	geobit4_ <- t(geobit4_)
	geobitMED2 <- median(geobit2)
	geobitMED3 <- median(geobit3)
	geobitMED4 <- median(geobit4)
	geobitMAX2 <- max(max(geobit2), max(geobit2_))
	geobitMAX3 <- max(max(geobit3), max(geobit3_))
	geobitMAX4 <- max(max(geobit4), max(geobit4_))
	geobitMED2_ <- median(geobit2_)
	geobitMED3_ <- median(geobit3_)
	geobitMED4_ <- median(geobit4_)

	medianMatrix <- matrix(c(uncompressedMED2, uncompressedMED3, uncompressedMED4, bitwiseMED2, bitwiseMED3, bitwiseMED4, geometricMED2, geometricMED3, geometricMED4, geobitMED2, geobitMED3, geobitMED4), nrow=3, ncol=4)

	medianMatrix_ <- matrix(c(uncompressedMED2_, uncompressedMED3_, uncompressedMED4_, bitwiseMED2_, bitwiseMED3_, bitwiseMED4_, geometricMED2_, geometricMED3_, geometricMED4_, geobitMED2_, geobitMED3_, geobitMED4_), nrow=3, ncol=4)

	maxMatrix <- matrix(c(uncompressedMAX2, uncompressedMAX3, uncompressedMAX4, bitwiseMAX2, bitwiseMAX3, bitwiseMAX4, geometricMAX2, geometricMAX3, geometricMAX4, geobitMAX2, geobitMAX3, geobitMAX4), nrow=3, ncol=4)

	medianMatrix <- t(medianMatrix)
	medianMatrix_ <- t(medianMatrix_)
	maxMatrix <- t(maxMatrix)

	maxMatrix

	#boxplot(cbind(t(uncompressed2),t(uncompressed3),t(uncompressed4)),xaxt="n")
	#boxplot(cbind(t(bitwise2),t(bitwise3),t(bitwise4)),xaxt="n")
	#boxplot(cbind(t(geometric2),t(geometric3),t(geometric4)),xaxt="n")
	#boxplot(cbind(t(geobit2),t(geobit3),t(geobit4)),xaxt="n")
	#axis(1,at=1:3,labels=c(1,2,3))
}

timeToExpansionRatio <- function(SubFigureAxisScaling) {
	df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
	
	DijkstraTime  <- (subset(df,Algorithm=="Dijkstra"))[,"AlgorithmTime"]
	AStarTime  <- (subset(df,Algorithm=="AStar"))[,"AlgorithmTime"]
	AStarSmoothedTime  <- (subset(df,Algorithm=="AStarSmoothed"))[,"AlgorithmTime"]
	ThetaStarTime <- (subset(df,Algorithm=="ThetaStar"))[,"AlgorithmTime"]
	LazyThetaStarTime  <- (subset(df,Algorithm=="LazyThetaStar"))[,"AlgorithmTime"]
	BlockAStarTime <- (subset(df,Algorithm=="LazyThetaStar"))[,"AlgorithmTime"]

	DijkstraNodes  <- (subset(df,Algorithm=="Dijkstra"))[,"NodesExpanded"]
	AStarNodes  <- (subset(df,Algorithm=="AStar"))[,"NodesExpanded"]
	AStarSmoothedNodes  <- (subset(df,Algorithm=="AStarSmoothed"))[,"NodesExpanded"]
	ThetaStarNodes  <- (subset(df,Algorithm=="ThetaStar"))[,"NodesExpanded"]
	LazyThetaStarNodes  <- (subset(df,Algorithm=="LazyThetaStar"))[,"NodesExpanded"]

	BlockAStarNodes <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar3N.csv")
	BlockAStarNodes <- BlockAStarNodes[,"NodesExpanded"]
	BlockAStarBlocks <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar3B.csv")
	BlockAStarBlocks <- BlockAStarBlocks[,"NodesExpanded"]

	Dijkstra <- DijkstraTime/DijkstraNodes
	AStar <- AStarTime/AStarNodes
	AStarSmoothed <- AStarSmoothedTime/AStarSmoothedNodes
	ThetaStar <- ThetaStarTime/ThetaStarNodes
	LazyThetaStar <- LazyThetaStarTime/LazyThetaStarNodes
	BASNodes <- BlockAStarTime/BlockAStarNodes
	BASBlocks <- BlockAStarTime/BlockAStarBlocks

	vector <- cbind(Dijkstra,AStar,AStarSmoothed,ThetaStar,LazyThetaStar,BASNodes,BASBlocks)
	
	par(mar=c(2.5,3,1,1))
	boxplot(vector * 1000, outline=FALSE, las=1, xaxt="n", cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling,col="grey", outcol="black", outpch=19,outcex=0.5, ann=FALSE)
	mtext("Execution time/Expansion / $\\mu$s",side=2,line=2,cex= SubFigureAxisScaling)
	axis(1, at=1:7, labels=c(DijkstraLabel, AStarLabel, AStarSmoothedLabel,ThetaStarLabel,LazyThetaStarLabel, "{\\em Block A*}", "{\\em Block A*}"), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
}

