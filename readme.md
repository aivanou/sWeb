owftnewft
wft
wft
wft
wf
tw
ft
wft
wft
Project:
noarsntt
arst
arst
ar
td
arsd
sr
da
rsd

Spec:
1. should be protocol agnostic
2. based on actors
3. easy to add/remove new servers,actors or threads
4. reliable(if the server is shutdown or the task did not execute, it should redestribute it to another server)
5.fault tolerant(the termination should not affect the cluster state)
6. easy to configure
7.

stuff to use:
1. netty
2. akka actors
3. zookeeper? (for distributed purposes)
