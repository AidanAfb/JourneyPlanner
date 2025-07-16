# Assignment 3 - Journey Planner

# The Risk Map

The Map of the Game Risk!, a classic strategy board game where players use dice to conquer territories. Don’t worry, you won’t be building the game itself; we’re only using the map.


I’m not sure how popular this game is in New Zealand, especially since New Zealand is missing from the map. Unfortunately, this is a surprisingly common oversight. Check out Maps Without NZ if you’re curious (or frustrated).


This map is a simplified version of the world, featuring 42 countries grouped into 6 continents: Europe, North America, South America, Africa, Asia, and Australia. The gray lines indicate connections between countries that can be reached via land or water.


A Graph is an ideal data structure to represent this map. In our graph, each country is a node (vertex), and edges represent direct connections (neighbors). For instance, Brazil is connected to Venezuela, North Africa, Argentina, and Peru, so it has edges to those countries. There’s no edge between India and Siberia because they are not directly connected. This neighbor relationship is symmetric—if A is connected to B, then B is connected to A.


By traversing this graph, we can simulate routing across countries. For example, to get from Congo to Brazil, a valid path is Congo → North Africa → Brazil. You can’t jump directly between non-adjacent countries like Congo and Brazil without passing through their shared neighbors.


Your task for Assignment 3 is to write code that loads this map into a graph and finds the shortest path between two countries (in terms of number of country visited).


In addition, you’ll notice that each country on the Risk map has an associated number. In the original game, these are used for reinforcement bonuses. In this assignment, these numbers represent the fuel cost to traverse through a country. Importantly, you only pay fuel costs for intermediate countries—not for the starting point or your final destination.


For example, to travel from Congo to Brazil via North Africa, you only pay the fuel cost for North Africa (5 units). You do not pay for Congo or Brazil, because they are the starting and ending points, respectively. So the total fuel cost for that route is 5 units. You need to compute and show the fuel consumption of the shortest path between two countries.


This assignment simulates a real case scenario where a company offering international delivery wants to implement software to find the optimal routing across the world. The map would be more complex, but they will definitely use a Graph data structure. Indeed, the underlying principle—using a graph to model routes and optimize costs—remains the same.
