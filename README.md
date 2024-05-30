# Matching Simulation

## Components of the simulation system
### AgentsFactory
Methods of Loading distribution files and creating agents
### ClassificationModel
Methods of loading prediction models and use them for predicting
### User
Abstract class for all agents
### Listener
Class of listener, inheriting class of User
### Member
Class of member, inheriting class of User
### Machi
The main simulation system
### Pair
Class of member-listener pair
### SimulationPeriodBaseline
One Minute in baseline simulation reproducing the reality
### SimulationPeriod
One Minute in simulation using different algorithm
### SimulationPeriodFilter
One Minute in simulation with teenager & gender-minority filters (teenagers only chat with teenagers, so do gender-minority members)
### Sort(Member/Listener)By...
Comparator to create member/listener's preference list

## Play with the simulation
You can playing with the simulation by changing the way members rank listeners in their personal preference list and the way listeners rank members in their personal preference list.(E.g. members prefer listeners in same gender, similar age etc.)

By default, agents are ranked based on creation time. In order to change the ranking, write a new Class implementing the Comparator interface and override the compare method. If the sorting method does not distinguish between agents, write it in the same Class (like **SortBySimilarity**), if it does distinguish, specify members and listeners separetely(like the **SortListenerByTopic** and **SortMembersByTopic** Class).

After implementing new Comparator Class for ranking agents, using the comparator at **SimulationPeriod**(or **SimulationPeriodFilter**, depends on whether the constraints of the talking only-within groups is used) at the begining of the ***Match()*** method. Method ***TopicMatching*** is a good example.
To change other setting such as gender distribution, change the part in the corresponding class but do not change the other parts unless necessary. 