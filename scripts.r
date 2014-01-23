boxPlotTime<-function(string) {
	df <- read.csv(paste("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/",string, sep=""))
	Dijkstra  <- (subset(df,Algorithm=="Dijkstra" & Map != "Map 0"))$AlgorithmTime
	AStar  <- (subset(df,Algorithm=="AStar" & Map != "Map 0"))$AlgorithmTime
	AStarSmoothed  <- (subset(df,Algorithm=="AStarSmoothed" & Map != "Map 0"))$AlgorithmTime
	ThetaStar  <- (subset(df,Algorithm=="ThetaStar" & Map != "Map 0"))$AlgorithmTime
	LazyThetaStar  <- (subset(df,Algorithm=="LazyThetaStar" & Map != "Map 0"))$AlgorithmTime
	BlockAStar  <- (subset(df,Algorithm=="BlockAStar" & Map != "Map 0"))$AlgorithmTime
	plot1 <- cbind(Dijkstra,AStar,AStarSmoothed,ThetaStar,LazyThetaStar,BlockAStar)
	boxplot(plot1,xlab="Algorithm", ylab="Time to computer path /ms", expand = 0.5, outline=FALSE)
}

boxPlotDistance <-function(string) {
	df <- read.csv(paste("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/",string, sep=""))
	Dijkstra  <- (subset(df,Algorithm=="Dijkstra" & Map != "Map 0"))$Distance
	AStar  <- (subset(df,Algorithm=="AStar" & Map != "Map 0"))$Distance
	AStarSmoothed  <- (subset(df,Algorithm=="AStarSmoothed" & Map != "Map 0"))$Distance
	ThetaStar  <- (subset(df,Algorithm=="ThetaStar" & Map != "Map 0"))$Distance
	LazyThetaStar  <- (subset(df,Algorithm=="LazyThetaStar" & Map != "Map 0"))$Distance
	BlockAStar  <- (subset(df,Algorithm=="BlockAStar" & Map != "Map 0"))$Distance
	plot1 <- cbind(Dijkstra,AStar,AStarSmoothed,ThetaStar,LazyThetaStar,BlockAStar)
	boxplot(plot1,xlab="Algorithm", ylab="Length of path", expand = 0.5)
}



