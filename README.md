#  Talend component tDenormalizeInChunks
This component takes values from a choosen incoming column and denormalize them. 

This meaning put these values in a chain. This chain can be used e.g. for an in clause in a sql select and this way you can reduce the amount of queries drastically.

You can configure how much values are in that chain at once.
It is important to provide the information how many records will come in or you have to send a so called end-marker record (e.g. null value as very last record).

You can also setup the delimiter between the values in the out going line (the denormalized value chain) and also setup an eclosure for the value.
