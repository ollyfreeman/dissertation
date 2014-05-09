nfig41 <-function() {
    require(reshape2)
    require(ggplot2)
    require(scales)
    
    df <- read.csv("/Users/olly_freeman/Documents/Part2/Dissertation/CSV/ValidPaths_EmptySandG_100_400_Cluster0to10to100.csv")
    df1000 <- read.csv("/Users/olly_freeman/Documents/Part2/Dissertation/CSV/ValidPaths_EmptySandG_100_400_Cluster1000.csv")
    
    Clustering0  <- (subset(df,Clustering==0))
    Clustering1  <- (subset(df,Clustering==100))
    Clustering2  <- (subset(df1000,Clustering==1000))
    Coverage <- Clustering0$Coverage
    Count0 <- Clustering0$CountFreeSandG
    Count1 <- Clustering1$CountFreeSandG
    Count2 <- Clustering2$CountFreeSandG

    DF <- data.frame(Coverage=Clustering0$Coverage, D0 = Count0, D100 = Count1, D1000 = Count2)
    
    DF <- melt(DF, id="Coverage")
    
    DF$variable<-factor(DF$variable, levels=c("D0","D100","D1000"), labels=c("$D=0$","$D=100$", "$D=1000$"))
    DF$value <- as.numeric(DF$value)
    
    n <- 400
    DF$se <- with(DF, sqrt(value*(1-value)/n))
    DF$ymax <- with(DF, value+1.96*se)
    DF$ymin <- with(DF, value-1.96*se)

    p <- ggplot(DF, aes(x=Coverage/100, y=value, colour=variable))
    p <- p + geom_line(linetype="dotted") + geom_point(size=1) + geom_errorbar(aes(ymax=ymax, ymin=ymin),width=0.02)
    p <- p + guides(colour=guide_legend(title="Clustering score"))
    percent_format2 <- function() {
        function(x, digits = 0, format = "f", ...) 
        { 
            paste(formatC(100 * x, format = format, digits = digits, ...), "\\%", sep = "") 
        }
    }
    p <- p + scale_x_continuous(labels=percent_format2())
    p <- p + labs(x="Coverage percentage, $C$", y="Proportion of maps with a valid path")
    p <- p + theme(legend.position=c(1.0,1.0), legend.justification=c(1,1),axis.title=element_text(size=10.5))+ opts(axis.title.y = theme_text(vjust=0.2),axis.title.x = theme_text(vjust=0.2))

    options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")
    
    #tikz('nfig4.1.tex', width=6.5, height=3.5)
    print(p)
    #dev.off()

}

nfig42<-function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
    
    AStarVis <- (subset(df,Algorithm=="AStarVisibility"))[,"Distance"]
    Dijkstra  <- (subset(df,Algorithm=="Dijkstra"))[,"Distance"]
    AStar  <- (subset(df,Algorithm=="AStar"))[,"Distance"]
    AStarSmoothed  <- (subset(df,Algorithm=="AStarSmoothed"))[,"Distance"]
    ThetaStar  <- (subset(df,Algorithm=="ThetaStar"))[,"Distance"]
    LazyThetaStar  <- (subset(df,Algorithm=="LazyThetaStar"))[,"Distance"]
    BlockAStar  <- (subset(df,Algorithm=="BlockAStar"))[,"Distance"]
    
    Dijkstra <- Dijkstra/AStarVis
    AStar  <- AStar/AStarVis
    AStarSmoothed <- AStarSmoothed/AStarVis
    ThetaStar  <- ThetaStar/AStarVis
    LazyThetaStar  <- LazyThetaStar/AStarVis
    BlockAStar  <- BlockAStar/AStarVis
		
    DF <- data.frame(Dijkstra=Dijkstra, AStar=AStar, AStarSmoothed=AStarSmoothed,
                     ThetaStar=ThetaStar, LazyThetaStar=LazyThetaStar, BlockAStar=BlockAStar)
    DF <- melt(DF)
    DF$variable<-factor(DF$variable,
                        levels=c("Dijkstra",       "AStar",    "AStarSmoothed","ThetaStar",    "LazyThetaStar",     "BlockAStar"),
                        labels=c("{\\em Dijkstra}","{\\em A*}","{\\em A* wps}","{\\em Theta*}","{\\em Lazy Theta*}","{\\em Block A*}"))
    p <-  ggplot(DF)
    p <- p + geom_boxplot(aes(x=variable,y=value))
    p <- p + labs(x="", y="Path length/Optimal path length")
    p <- p + theme(axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))

    options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.2.tex', width=6.5, height=4.5)
    print(p)
    #dev.off()
}

nfig43a <- function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
    
    AStarVis <- (subset(df,Algorithm=="AStarVisibility"))[,"Distance"]
    AStarSmoothed  <- (subset(df,Algorithm=="AStarSmoothed"))[,"Distance"]
    ThetaStar  <- (subset(df,Algorithm=="ThetaStar"))[,"Distance"]
    LazyThetaStar  <- (subset(df,Algorithm=="LazyThetaStar"))[,"Distance"]
    BlockAStar  <- (subset(df,Algorithm=="BlockAStar"))[,"Distance"]
    
    AStarSmoothed <- AStarSmoothed/AStarVis
    ThetaStar  <- ThetaStar/AStarVis
    LazyThetaStar  <- LazyThetaStar/AStarVis
    BlockAStar  <- BlockAStar/AStarVis
		
    DF <- data.frame(AStarSmoothed=AStarSmoothed,
                     ThetaStar=ThetaStar, LazyThetaStar=LazyThetaStar, BlockAStar=BlockAStar)
    DF <- melt(DF)
    DF$variable<-factor(DF$variable,
                        levels=c("AStarSmoothed","ThetaStar",    "LazyThetaStar",     "BlockAStar"),
                        labels=c("{\\em A* wps}","{\\em Theta*}","{\\em Lazy}\n{\\em Theta*}","{\\em Block A*}"))
    p <-  ggplot(DF)
    p <- p + geom_boxplot(aes(x=variable,y=value))
    p <- p + labs(x="", y="Path length/Optimal path length")
    p <- p + theme(axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))

    options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.3.a.tex', width=3.5, height=3.5)
    print(p)
    #dev.off()

}

nfig43b <- function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
    
    AStarVis <- (subset(df,Algorithm=="AStarVisibility"))[,"Distance"]
    Dijkstra  <- (subset(df,Algorithm=="Dijkstra"))[,"Distance"]
    AStar  <- (subset(df,Algorithm=="AStar"))[,"Distance"]
    
    Dijkstra <- Dijkstra/AStarVis
    AStar  <- AStar/AStarVis
		
    DF <- data.frame(Dijkstra=Dijkstra, AStar=AStar)

    maximum <- max(DF); minimum <- min(DF)

    DF$r.val <- with(DF, (maximum - Dijkstra)*0.999/(maximum-minimum))

    p <- ggplot(DF)
    p <- p + geom_segment(aes(x=1,xend=2,y=Dijkstra,yend=AStar, colour=1-r.val))
    p <- p + geom_point(aes(x=1,y=Dijkstra, colour=1-r.val), shape=4)
    p <- p + geom_point(aes(x=2,y=AStar, colour=1-r.val), shape=4)
    p <- p + scale_x_continuous(breaks=c(1,2),labels=c("\\em Dijkstra","\\em A*"))
    p <- p + scale_color_gradient(low="red",high="blue")
    p <- p + labs(x="",y="Path length/Optimal path length")
    p <- p + theme(legend.position="none",axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))

    options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.3.b.tex', width=3, height=3.5, sanitize=TRUE)
    print(p)
    #dev.off()

}

nfig44 <- function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
    
    AStarVis <- (subset(df,Algorithm=="AStarVisibility"))[,"Angle"]
    Dijkstra  <- (subset(df,Algorithm=="Dijkstra"))[,"Angle"]
    AStar  <- (subset(df,Algorithm=="AStar"))[,"Angle"]
    AStarSmoothed  <- (subset(df,Algorithm=="AStarSmoothed"))[,"Angle"]
    ThetaStar  <- (subset(df,Algorithm=="ThetaStar"))[,"Angle"]
    LazyThetaStar  <- (subset(df,Algorithm=="LazyThetaStar"))[,"Angle"]
    BlockAStar  <- (subset(df,Algorithm=="BlockAStar"))[,"Angle"]
    
    Dijkstra <- Dijkstra/AStarVis
    AStar  <- AStar/AStarVis
    AStarSmoothed <- AStarSmoothed/AStarVis
    ThetaStar  <- ThetaStar/AStarVis
    LazyThetaStar  <- LazyThetaStar/AStarVis
    BlockAStar  <- BlockAStar/AStarVis
		
    DF <- data.frame(Dijkstra=Dijkstra, AStar=AStar, AStarSmoothed=AStarSmoothed,
                     ThetaStar=ThetaStar, LazyThetaStar=LazyThetaStar, BlockAStar=BlockAStar)
    DF <- melt(DF)
    DF$variable<-factor(DF$variable,
                        levels=c("Dijkstra",       "AStar",    "AStarSmoothed","ThetaStar",    "LazyThetaStar",     "BlockAStar"),
                        labels=c("{\\em Dijkstra}","{\\em A*}","{\\em A* wps}","{\\em Theta*}","{\\em Lazy Theta*}","{\\em Block A*}"))
    p <-  ggplot(DF)
    p <- p + geom_boxplot(aes(x=variable,y=value))
    p <- p + labs(x="", y="Path angle-sum/Optimal path angle-sum")
    p <- p + theme(axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))


    options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.4.tex', width=6.5, height=4.5)
    print(p)
    #dev.off()

}

nfig45a <- function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
    
    AStarVis <- (subset(df,Algorithm=="AStarVisibility"))[,"Angle"]
    AStarSmoothed  <- (subset(df,Algorithm=="AStarSmoothed"))[,"Angle"]
    ThetaStar  <- (subset(df,Algorithm=="ThetaStar"))[,"Angle"]
    LazyThetaStar  <- (subset(df,Algorithm=="LazyThetaStar"))[,"Angle"]
    BlockAStar  <- (subset(df,Algorithm=="BlockAStar"))[,"Angle"]
    
    AStarSmoothed <- AStarSmoothed/AStarVis
    ThetaStar  <- ThetaStar/AStarVis
    LazyThetaStar  <- LazyThetaStar/AStarVis
    BlockAStar  <- BlockAStar/AStarVis
		
    DF <- data.frame(AStarSmoothed=AStarSmoothed,
                     ThetaStar=ThetaStar, LazyThetaStar=LazyThetaStar, BlockAStar=BlockAStar)
    DF <- melt(DF)
    DF$variable<-factor(DF$variable,
                        levels=c("AStarSmoothed","ThetaStar",    "LazyThetaStar",     "BlockAStar"),
                        labels=c("{\\em A* wps}","{\\em Theta*}","{\\em Lazy}\n{\\em Theta*}","{\\em Block A*}"))
    p <-  ggplot(DF)
    p <- p + geom_boxplot(aes(x=variable,y=value))
    p <- p + labs(x="", y="Path angle-sum/Optimal path angle-sum")
    p <- p + theme(axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))

    options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.5.a.tex', width=3.25, height=4.5)
    print(p)
    #dev.off()

}

nfig46b <- function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
    
    AStarVis <- (subset(df,Algorithm=="AStarVisibility"))[,"Distance"]

    BlockAStar2 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar2.csv")[,"Distance"]
    BlockAStar3 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar3.csv")[,"Distance"]
    BlockAStar4 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar4.csv")[,"Distance"]

    BlockAStar2 <- BlockAStar2/AStarVis
    BlockAStar3 <- BlockAStar3/AStarVis
    BlockAStar4 <- BlockAStar4/AStarVis

    DF <- data.frame(BlockAStar2=BlockAStar2, BlockAStar3=BlockAStar3, BlockAStar4=BlockAStar4)

    maximum <- max(DF); minimum <- min(DF)

    DF$r.val <- with(DF, (maximum - BlockAStar2)*0.999/(maximum-minimum))

    p <- ggplot(DF)
    p <- p + geom_segment(aes(x=2,xend=3,y=BlockAStar2,yend=BlockAStar3, colour=1-r.val))
    p <- p + geom_segment(aes(x=3,xend=4,y=BlockAStar3,yend=BlockAStar4, colour=1-r.val))
    p <- p + geom_point(aes(x=2,y=BlockAStar2, colour=1-r.val), shape=4)
    p <- p + geom_point(aes(x=3,y=BlockAStar3, colour=1-r.val), shape=4)
    p <- p + geom_point(aes(x=4,y=BlockAStar4, colour=1-r.val), shape=4)
    p <- p + scale_x_continuous(breaks=c(2,3,4),labels=c("2","3","4"))
    p <- p + ylim(1.0,1.015)
    p <- p + scale_color_gradient(low="red",high="blue")
    p <- p + labs(x="",y="Path length/Optimal path length")
    p <- p + theme(legend.position="none",axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))

    options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.6.b.tex', width=3.25, height=4.5)
    print(p)
    #dev.off()

}

nfig47a <- function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
    
    Dijkstra  <- (subset(df,Algorithm=="Dijkstra"))[,"NodesExpanded"]/1000
    AStar  <- (subset(df,Algorithm=="AStar"))[,"NodesExpanded"]/1000
    AStarSmoothed  <- (subset(df,Algorithm=="AStarSmoothed"))[,"NodesExpanded"]/1000
    ThetaStar  <- (subset(df,Algorithm=="ThetaStar"))[,"NodesExpanded"]/1000
    LazyThetaStar  <- (subset(df,Algorithm=="LazyThetaStar"))[,"NodesExpanded"]/1000
    
    DF <- data.frame(Dijkstra=Dijkstra, AStar=AStar, AStarSmoothed=AStarSmoothed,
                     ThetaStar=ThetaStar, LazyThetaStar=LazyThetaStar)

     maximum <- max(DF); minimum <- min(DF)

    DF$r.val <- with(DF, (maximum - LazyThetaStar)*0.999/(maximum-minimum))

    p <- ggplot(DF)
    p <- p + geom_segment(aes(x=1,xend=2,y=Dijkstra,yend=AStar, colour=1-r.val))
    p <- p + geom_segment(aes(x=2,xend=3,y=AStar,yend=AStarSmoothed, colour=1-r.val))
    p <- p + geom_segment(aes(x=3,xend=4,y=AStarSmoothed,yend=ThetaStar, colour=1-r.val))
    p <- p + geom_segment(aes(x=4,xend=5,y=ThetaStar,yend=LazyThetaStar, colour=1-r.val))
    p <- p + geom_point(aes(x=1,y=Dijkstra, colour=1-r.val), shape=4)
    p <- p + geom_point(aes(x=2,y=AStar, colour=1-r.val), shape=4)
    p <- p + geom_point(aes(x=3,y=AStarSmoothed, colour=1-r.val), shape=4)
    p <- p + geom_point(aes(x=4,y=ThetaStar, colour=1-r.val), shape=4)
    p <- p + geom_point(aes(x=5,y=LazyThetaStar, colour=1-r.val), shape=4)
    p <- p + scale_x_continuous(breaks=c(1,2,3,4,5),labels=c("\\em Dijkstra","\\em A*","\\em A* wps","\\em Theta*","\\em Lazy\n\\em Theta*"))
    p <- p + scale_color_gradient(low="red",high="blue")
    p <- p + labs(x="",y="Expansions")
    p <- p + theme(legend.position="none",axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))

    options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.7.a.tex', width=3.25, height=4.5)
    print(p)
    #dev.off()

}

nfig48a <- function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
    
    AStar  <- (subset(df,Algorithm=="AStar"))[,"NodesExpanded"]/1000
    ThetaStar  <- (subset(df,Algorithm=="ThetaStar"))[,"NodesExpanded"]/1000
    BlockAStar  <- (subset(df,Algorithm=="BlockAStar"))[,"NodesExpanded"]/1000
    
    DF <- data.frame(AStar=AStar,
                     ThetaStar=ThetaStar, BlockAStar=BlockAStar)

     maximum <- max(DF); minimum <- min(DF)

    DF$r.val <- with(DF, (maximum - AStar)*0.999/(maximum-minimum))

    p <- ggplot(DF)
     p <- p + geom_segment(aes(x=1,xend=2,y=AStar,yend=ThetaStar, colour=1-r.val))
     p <- p + geom_segment(aes(x=2,xend=3,y=ThetaStar,yend=BlockAStar, colour=1-r.val))
    p <- p + geom_point(aes(x=1,y=AStar, colour=1-r.val), shape=4)
     p <- p + geom_point(aes(x=2,y=ThetaStar, colour=1-r.val), shape=4)
     p <- p + geom_point(aes(x=3,y=BlockAStar, colour=1-r.val), shape=4)
    p <- p + scale_x_continuous(breaks=c(1,2,3),labels=c("\\em A*","\\em Theta*","\\em Block A*"))
    p <- p + scale_color_gradient(low="red",high="blue")
    p <- p + labs(x="",y="Expansions $\\times 10^{3}$")
    p <- p + theme(legend.position="none",axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))


    options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.8.a.tex', width=3.25, height=4.5)
    print(p)
    #dev.off()

}

nfig48b <- function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
    
    BlockAStar2 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar2.csv")[,"BlocksExpanded"]/1000
    BlockAStar3 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar3.csv")[,"BlocksExpanded"]/1000
    BlockAStar4 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar4.csv")[,"BlocksExpanded"]/1000

    DF <- data.frame(BlockAStar2=BlockAStar2, BlockAStar3=BlockAStar3, BlockAStar4=BlockAStar4)

    maximum <- max(DF); minimum <- min(DF)

    DF$r.val <- with(DF, (maximum - BlockAStar2)*0.999/(maximum-minimum))

    p <- ggplot(DF)
    p <- p + geom_segment(aes(x=2,xend=3,y=BlockAStar2,yend=BlockAStar3, colour=1-r.val))
    p <- p + geom_segment(aes(x=3,xend=4,y=BlockAStar3,yend=BlockAStar4, colour=1-r.val))
    p <- p + geom_point(aes(x=2,y=BlockAStar2, colour=1-r.val), shape=4)
    p <- p + geom_point(aes(x=3,y=BlockAStar3, colour=1-r.val), shape=4)
    p <- p + geom_point(aes(x=4,y=BlockAStar4, colour=1-r.val), shape=4)
    p <- p + scale_x_continuous(breaks=c(2,3,4),labels=c("2","3","4"))
    p <- p + scale_color_gradient(low="red",high="blue")
    p <- p + labs(x="",y="Expansions $\\times 10^{3}$")
    p <- p + theme(legend.position="none",axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))


    #options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.8.b.tex', width=3.25, height=4.5)
    print(p)
    #dev.off()

}

nfig49a <- function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
    BlockAStar <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar4.csv")
    
    AStar  <- (subset(df,Algorithm=="AStar"))[,"NodesExpanded"]/1000
    ThetaStar  <- (subset(df,Algorithm=="ThetaStar"))[,"NodesExpanded"]/1000
    BlockAStar <- (subset(BlockAStar,Algorithm=="BlockAStar"))[,"NodesExpanded"]/1000
    
    DF <- data.frame(AStar=AStar,
                     ThetaStar=ThetaStar, BlockAStar=BlockAStar)

     maximum <- max(DF); minimum <- min(DF)

    DF$r.val <- with(DF, (maximum - AStar)*0.999/(maximum-minimum))

    p <- ggplot(DF)
     p <- p + geom_segment(aes(x=1,xend=2,y=AStar,yend=ThetaStar, colour=1-r.val))
     p <- p + geom_segment(aes(x=2,xend=3,y=ThetaStar,yend=BlockAStar, colour=1-r.val))
    p <- p + geom_point(aes(x=1,y=AStar, colour=1-r.val), shape=4)
     p <- p + geom_point(aes(x=2,y=ThetaStar, colour=1-r.val), shape=4)
     p <- p + geom_point(aes(x=3,y=BlockAStar, colour=1-r.val), shape=4)
    p <- p + scale_x_continuous(breaks=c(1,2,3),labels=c("\\em A*","\\em Theta*","\\em Block A*"))
    p <- p + scale_color_gradient(low="red",high="blue")
    p <- p + labs(x="",y="Expansions $\\times 10^{3}$")
    p <- p + theme(legend.position="none",axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))


    options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.9.a.tex', width=6.5, height=4.5)
    print(p)
    #dev.off()

}

nfig49b <- function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
    BlockAStar <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar4.csv")
    
    AStar  <- (subset(df,Algorithm=="AStar"))[,"NodesExpanded"]/1000
    ThetaStar  <- (subset(df,Algorithm=="ThetaStar"))[,"NodesExpanded"]/1000
    BlockAStar  <- (subset(BlockAStar,Algorithm=="BlockAStar"))[,"NodesExpanded"]/1000

    AStar <- AStar*8
    ThetaStar <- ThetaStar*8
    BlockAStar <- BlockAStar*16
    
    DF <- data.frame(AStar=AStar,
                     ThetaStar=ThetaStar, BlockAStar=BlockAStar)

     maximum <- max(DF); minimum <- min(DF)

    DF$r.val <- with(DF, (maximum - AStar)*0.999/(maximum-minimum))

    p <- ggplot(DF)
     p <- p + geom_segment(aes(x=1,xend=2,y=AStar,yend=ThetaStar, colour=1-r.val))
     p <- p + geom_segment(aes(x=2,xend=3,y=ThetaStar,yend=BlockAStar, colour=1-r.val))
    p <- p + geom_point(aes(x=1,y=AStar, colour=1-r.val), shape=4)
     p <- p + geom_point(aes(x=2,y=ThetaStar, colour=1-r.val), shape=4)
     p <- p + geom_point(aes(x=3,y=BlockAStar, colour=1-r.val), shape=4)
    p <- p + scale_x_continuous(breaks=c(1,2,3),labels=c("\\em A*","\\em Theta*","\\em Block A*"))
    p <- p + scale_color_gradient(low="red",high="blue")
    p <- p + labs(x="",y="Neighbour Vists $\\times 10^{3}$")
    p <- p + theme(legend.position="none",axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))


    options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.9.b.tex', width=6.5, height=4.5)
    print(p)
    #dev.off()

}

nfig49c <- function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
    
    BlockAStar2 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar2.csv")[,"NeighbourVisits"]/1000
    BlockAStar3 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar3.csv")[,"NeighbourVisits"]/1000
    BlockAStar4 <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar4.csv")[,"NeighbourVisits"]/1000

    DF <- data.frame(BlockAStar2=BlockAStar2, BlockAStar3=BlockAStar3, BlockAStar4=BlockAStar4)

    maximum <- max(DF); minimum <- min(DF)

    DF$r.val <- with(DF, (maximum - BlockAStar2)*0.999/(maximum-minimum))

    p <- ggplot(DF)
    p <- p + geom_segment(aes(x=2,xend=3,y=BlockAStar2,yend=BlockAStar3, colour=1-r.val))
    p <- p + geom_segment(aes(x=3,xend=4,y=BlockAStar3,yend=BlockAStar4, colour=1-r.val))
    p <- p + geom_point(aes(x=2,y=BlockAStar2, colour=1-r.val), shape=4)
    p <- p + geom_point(aes(x=3,y=BlockAStar3, colour=1-r.val), shape=4)
    p <- p + geom_point(aes(x=4,y=BlockAStar4, colour=1-r.val), shape=4)
    p <- p + scale_x_continuous(breaks=c(2,3,4),labels=c("2","3","4"))
    p <- p + scale_color_gradient(low="red",high="blue")
    p <- p + labs(x="",y="Neighbour Visits $\\times 10^{3}$")
    p <- p + theme(legend.position="none",axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))


    #options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.9.c.tex', width=3.25, height=4.5)
    print(p)
    #dev.off()

}



nfig410 <- function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
    
    Dijkstra  <- (subset(df,Algorithm=="Dijkstra"))[,"AlgorithmTime"]
    AStar  <- (subset(df,Algorithm=="AStar"))[,"AlgorithmTime"]
    AStarSmoothed  <- (subset(df,Algorithm=="AStarSmoothed"))[,"AlgorithmTime"]
    ThetaStar  <- (subset(df,Algorithm=="ThetaStar"))[,"AlgorithmTime"]
    LazyThetaStar  <- (subset(df,Algorithm=="LazyThetaStar"))[,"AlgorithmTime"]
    BlockAStar  <- (subset(df,Algorithm=="BlockAStar"))[,"AlgorithmTime"]
    
    DF <- data.frame(Dijkstra=Dijkstra, AStar=AStar, AStarSmoothed=AStarSmoothed,
                     ThetaStar=ThetaStar, LazyThetaStar=LazyThetaStar, BlockAStar=BlockAStar)
    DF <- melt(DF)
    DF$variable<-factor(DF$variable,
                        levels=c("Dijkstra",       "AStar",    "AStarSmoothed","ThetaStar",    "LazyThetaStar",     "BlockAStar"),
                        labels=c("{\\em Dijkstra}","{\\em A*}","{\\em A* wps}","{\\em Theta*}","{\\em Lazy Theta*}","{\\em Block A*}"))
    p <-  ggplot(DF)
    p <- p + geom_boxplot(aes(x=variable,y=value))
    p <- p + ylim(0,40)
    p <- p + labs(x="", y="Execution Time (ms)")
    p <- p + theme(axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))

    options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.10.tex', width=6.5, height=4.5)
    print(p)
    #dev.off()

}

nfig411 <- function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50.csv")
    blockAStarDF <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_blockAStar3.csv")
    
    Dijkstra  <- (subset(df,Algorithm=="Dijkstra"))[,"AlgorithmTime"]
    AStar  <- (subset(df,Algorithm=="AStar"))[,"AlgorithmTime"]
    AStarSmoothed  <- (subset(df,Algorithm=="AStarSmoothed"))[,"AlgorithmTime"]
    ThetaStar  <- (subset(df,Algorithm=="ThetaStar"))[,"AlgorithmTime"]
    LazyThetaStar  <- (subset(df,Algorithm=="LazyThetaStar"))[,"AlgorithmTime"]
    BlockAStarNodes  <- (subset(df,Algorithm=="BlockAStar"))[,"AlgorithmTime"]
    BlockAStarBlocks  <- (subset(df,Algorithm=="BlockAStar"))[,"AlgorithmTime"]

    Dijkstra <- Dijkstra/(subset(df,Algorithm=="Dijkstra"))[,"NodesExpanded"]*1000
    AStar <- AStar/(subset(df,Algorithm=="AStar"))[,"NodesExpanded"]*1000
    AStarSmoothed <- AStarSmoothed/(subset(df,Algorithm=="AStarSmoothed"))[,"NodesExpanded"]*1000
    ThetaStar <- ThetaStar/(subset(df,Algorithm=="ThetaStar"))[,"NodesExpanded"]*1000
    LazyThetaStar <- LazyThetaStar/(subset(df,Algorithm=="LazyThetaStar"))[,"NodesExpanded"]*1000
    BlockAStarNodes <- BlockAStarNodes/(subset(blockAStarDF,Algorithm=="BlockAStar"))[,"NodesExpanded"]*1000
    BlockAStarBlocks <- BlockAStarBlocks/(subset(blockAStarDF,Algorithm=="BlockAStar"))[,"BlocksExpanded"]*1000
    
    DF <- data.frame(Dijkstra=Dijkstra, AStar=AStar, AStarSmoothed=AStarSmoothed,
                     ThetaStar=ThetaStar, LazyThetaStar=LazyThetaStar,BlockAStarNodes=BlockAStarNodes,BlockAStarBlocks=BlockAStarBlocks)
    DF <- melt(DF)
    DF$variable<-factor(DF$variable,
                        levels=c("Dijkstra",       "AStar",    "AStarSmoothed","ThetaStar",    "LazyThetaStar", "BlockAStarNodes","BlockAStarBlocks"),
                        labels=c("{\\em Dijkstra}","{\\em A*}","{\\em A* wps}","{\\em Theta*}","{\\em Lazy Theta*}","{\\em Block A*N}","{\\em Block A*B}"))
    p <-  ggplot(DF)
    p <- p + geom_boxplot(aes(x=variable,y=value))

    p <- p + ylim(0,7.5)
    p <- p + labs(x="", y="Execution time/Expansion / $\\mu$s")
    p <- p + theme(axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))

    options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.11.tex', width=6.5, height=4.5)
    print(p)
    #dev.off()

}

nfig412 <- function() {
    require(ggplot2)
    require(reshape2)
    
    df <- read.csv("/Users/olly_freeman/Dropbox/Part2Project/maps/CSVs/200_20_50_priQ.csv")
    
    Dijkstra  <- (subset(df,Algorithm=="Dijkstra"))[,"QueueLength"]
    AStar  <- (subset(df,Algorithm=="AStar"))[,"QueueLength"]
    AStarSmoothed  <- (subset(df,Algorithm=="AStarSmoothed"))[,"QueueLength"]
    ThetaStar  <- (subset(df,Algorithm=="ThetaStar"))[,"QueueLength"]
    LazyThetaStar  <- (subset(df,Algorithm=="LazyThetaStar"))[,"QueueLength"]
    BlockAStar <- (subset(df,Algorithm=="BlockAStar"))[,"QueueLength"] 
    
    DF <- data.frame(Dijkstra=Dijkstra, AStar=AStar, AStarSmoothed=AStarSmoothed,
                     ThetaStar=ThetaStar, LazyThetaStar=LazyThetaStar,BlockAStar=BlockAStar)
    DF <- melt(DF)
    DF$variable<-factor(DF$variable,
                        levels=c("Dijkstra",       "AStar",    "AStarSmoothed","ThetaStar",    "LazyThetaStar", "BlockAStar"),
                        labels=c("{\\em Dijkstra}","{\\em A*}","{\\em A* wps}","{\\em Theta*}","{\\em Lazy Theta*}","{\\em Block A*}"))
    p <-  ggplot(DF)
    p <- p + geom_boxplot(aes(x=variable,y=value))

    p <- p + ylim(0,800)
    p <- p + labs(x="", y="Mean length of {\\em openSet}")
    p <- p + theme(axis.title=element_text(size=10.5)) + opts(axis.title.y = theme_text(vjust=0.4))

    options(tikzDocumentDeclaration = "\\documentclass[12pt]{article}\n")

    #tikz('nfig4.12.tex', width=6.5, height=4.5)
    print(p)
    #dev.off()

}
                     
