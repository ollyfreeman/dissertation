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
	} else if (Parameter == "AlgorithmTime") {
		ylabel <- "Execution Time/s"
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
		xlabel <- "Algorithm"
		if(ClassicAnyAllNoBASThree == "Classic") {
			plot1 <- cbind(Dijkstra,AStar)
			boxplot(plot1,xlab=xlabel, ylab=ylabel, expand = 0.5, outline=FALSE, las=1, xaxt="n", cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
			axis(1, at=1:2, labels=c(DijkstraLabel, AStarLabel), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		} else if (ClassicAnyAllNoBASThree == "Any") {
			plot1 <- cbind(AStarSmoothed,ThetaStar,LazyThetaStar,BlockAStar)
			boxplot(plot1,xlab=xlabel, ylab=ylabel, expand = 0.5, outline=FALSE, las=1, xaxt="n", cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
			axis(1, at=1:4, labels=c(AStarSmoothedLabel,ThetaStarLabel,LazyThetaStarLabel, BlockAStarLabel), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)		
		} else if (ClassicAnyAllNoBASThree == "All") {
			plot1 <- cbind(Dijkstra,AStar,AStarSmoothed,ThetaStar,LazyThetaStar,BlockAStar)
			boxplot(plot1,xlab=xlabel, ylab=ylabel, expand = 0.5, outline=FALSE, las=1, xaxt="n", cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
			axis(1, at=1:6, labels=c(DijkstraLabel,AStarLabel,AStarSmoothedLabel,ThetaStarLabel,LazyThetaStarLabel, BlockAStarLabel), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		} else if (ClassicAnyAllNoBASThree == "NoBAS"){
			plot1 <- cbind(Dijkstra,AStar,AStarSmoothed,ThetaStar,LazyThetaStar)
			boxplot(plot1,xlab=xlabel, ylab=ylabel, expand = 0.5, outline=FALSE, las=1, xaxt="n", cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
			axis(1, at=1:5, labels=c(DijkstraLabel,AStarLabel,AStarSmoothedLabel,ThetaStarLabel,LazyThetaStarLabel), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		} else {
			plot1 <- cbind(AStar,ThetaStar, BlockAStar)
			boxplot(plot1,xlab=xlabel, ylab=ylabel, expand = 0.5, outline=FALSE, las=1, xaxt="n", cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
			axis(1, at=1:3, labels=c(AStarLabel,ThetaStarLabel,BlockAStarLabel), cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
		}
	} else {
		xlabel <- "Block size"
		plot1 <- cbind(b2,b3,b4)
		boxplot(plot1,xlab=xlabel, ylab=ylabel, expand = 0.5, outline=FALSE, las=1, xaxt="n", cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
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
	} else if (Parameter == "Angle") {
		ylabel <- "Path angle-sum/degrees"
	} else if (Parameter == "AlgorithmTime") {
		ylabel <- "ExecutionTime/s"
	} else {
		ylabel <- "Expansions $\\times 10^{3}$"
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
		xlabel<- "Algorithm"
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
		xlabel<- "Block size"
		maximum <- max(b2)
		minimum <- 1
		scaler <- b2[1]
		xaxis = c(2,3,4)
		vector <- c(b2[1],b3[1],b4[1])
		ylimit <- max(max(b2),max(b3),max(b4))
	}
	red <- (maximum-scaler)*(0.999/(maximum-minimum))
	blue <- 1 - (maximum-scaler)*(0.999/(maximum-minimum))
	plot(xaxis, vector, col = rgb(red,0.0,blue), pch=4, ylim=c(1, ylimit), xaxt="n", xlab=xlabel, ylab = ylabel,  las=1, cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
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
	plot(c(2,3,4), (c(b2[1],b3[1],b4[1])*multiplier), col = rgb(red,0.0,blue), pch=4, ylim=c(0, max(max(b2*multiplier[1]),max(b3*multiplier[2]),max(b4*multiplier[3]))), xaxt="n", xlab="Block size", ylab = ylabel, las=1, cex.axis=SubFigureAxisScaling, cex.lab=SubFigureAxisScaling)
	axis(1, at=2:4, labels=c(2,3,4))
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