# heuristic-solution-for-knapsack-problem
A simple heuristic solution for knapsack problem using density properity and greedy approach

This algorithm works according to hill climbing  greedy aproach

The algorithm will start by making initial solution for the problem then replacing items in the solution with better choices

Algorithm:

step 1 : find density value of each item (item's price / item's weight).

step 2 : sort items accodring to density values. If number of items more than 5000 sort items partially (items not sorted but items with highest density).

step 3 : get items with highest density as initial solution.

step 4 : replace items in initial solution with items that have better price. keep repeating dozen of times



Result of steps above will not get perfect solution but solution will be acceptable and not too far from the perfect one
