# indoor-positioning
Project title: Indoor localization and navigation using Visible Light Communications.
Member: me 
Job:  Design a whole system
Language: Java and Arduino 
When: Dec. 2016 to Sep.  2017
Where: Aizu University
Why: Make a project base on interested IoT problem and to confirm my research.
Problems:
+ Real IPS based on VLC
How: 
+  Transmitter site: 
-	Design signal and make Arduino run it
-	Create Amplify circuit to guarantee the source for LED
+ Receiver site: 
-	Make a connection with smartphone
-	Decode signal, correct it and send it to smartphone
+ Smartphone: 
-	Bluetooth connection require 
-	Receive signal
-	Detect location and make a navigation by Dijkstra's algorithm in given graph 
-	Display the shortest path from current location to destination
Short explain: the transmitters include LED Arduino UNO circuit and amplify circuit always send a signal.
The smartphone will make a Bluetooth connection require to the receiver. Then after the connection is established the receiver will detect the signal from transmitter and correct it then send it to smartphone. The smartphone compare it to data base and know which node we are belong to and collect the destination node from User input. 
We have a database to store the nodes and edges in graph made from local map. It is multi-floor plan. And use two nodes with Dijkstra's algorithm the shortest path will display to user.
link of paper : https://ieeexplore.ieee.org/document/8517235
