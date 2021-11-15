<h1>RocksDb-Recipe</h1>
A repository for learning RocksDB not by reading but implementing Complete Java implementation of various operations on Rocks Db along with understanding of underlying concepts.

<H2> Recipe 1: Basic operations</H2>

Basic operations covered in this section are:

1. Opening of Database and various options which can be configured while creating the database..
2. Operations like Save, find, delete from a DB.
3. Logging of status of the database.
4. Retrieve multiple keys in single operation
5. Write batch operations to speed up the bul process. This is used for atomic updates, all or none operations will be performed with Write batch
6. Making write operation synchronous.

<H2>Recipe 2: Iterators</H2>
Various operations that can be performed with iterators are :

1. Seek To First
2. Seek To Last
3. Get All keys
4. Iterate from a specific key
5. Prefix Seek
6. SeeKForPrev
7. Tailing Iterator

Refer to this series of blogs for conceptual understanding

https://sunpriyakaurbhatia.medium.com/getting-started-with-rocks-db-part-1-conceptual-understanding-2d7131d37bc7

https://sunpriyakaurbhatia.medium.com/getting-started-with-rocks-db-part-2-project-setup-implementation-ccb48ae0eed6

https://sunpriyakaurbhatia.medium.com/getting-started-with-rocksdb-part-3-iterations-b2a3d6170e01