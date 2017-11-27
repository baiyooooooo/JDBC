-- DB URL --

RDBMS	JDBC驱动程序名称	URL格式
MySQL	com.mysql.jdbc.Driver	jdbc:mysql://hostname/databaseName
ORACLE	oracle.jdbc.driver.OracleDriver	jdbc:oracle:thin:@hostname:portNumber:databaseName
PostgreSQL	org.postgresql.Driver	jdbc:postgresql://hostname:port/dbname
DB2	com.ibm.db2.jdbc.net.DB2Driver	jdbc:db2:hostname:port Number/databaseName
Sybase	com.sybase.jdbc.SybDriver	jdbc:sybase:Tds:hostname: portNum

-- ResultSet --

createStatement(int RSType, int RSConcurrency);
prepareStatement(String SQL, int RSType, int RSConcurrency);
prepareCall(String sql, int RSType, int RSConcurrency);

可能的RSType值如下。如果不指定任何ResultSet类型，将自动分配一个TYPE_FORWARD_ONLY值。

类型	描述
ResultSet.TYPE_FORWARD_ONLY	光标只能在结果集中向前移动。
ResultSet.TYPE_SCROLL_INSENSITIVE	光标可以向前和向后滚动，结果集对创建结果集后发生的数据库所做的更改不敏感。
ResultSet.TYPE_SCROLL_SENSITIVE	光标可以向前和向后滚动，结果集对创建结果集之后发生的其他数据库的更改敏感。
ResultSet的并发性

可能的RSConcurrency如下。 如果不指定任何并发类型，将自动获得一个CONCUR_READ_ONLY值。

并发	描述
ResultSet.CONCUR_READ_ONLY	创建只读结果集，这是默认值。
ResultSet.CONCUR_UPDATABLE	创建可更新的结果集

-- 数据类型 --
SQL类型	JDBC/Java类型	setXXX	updateXXX
VARCHAR	java.lang.String	setString	getString
CHAR	java.lang.String	setString	getString
LONGVARCHAR	java.lang.String	setString	getString
BIT	boolean	setBoolean	getBoolean
NUMERIC	java.math.BigDecimal	setBigDecimal	getBigDecimal
TINYINT	byte	setByte	getByte
SMALLINT	short	setShort	getShort
INTEGER	int	setInt	getInt
BIGINT	long	setLong	getLong
REAL	float	setFloat	getFloat
FLOAT	float	setFloat	getFloat
DOUBLE	double	setDouble	getDouble
VARBINARY	byte[ ]	setBytes	getBytes
BINARY	byte[ ]	setBytes	getBytes
DATE	java.sql.Date	setDate	getDate
TIME	java.sql.Time	setTime	getTime
TIMESTAMP	java.sql.Timestamp	setTimestamp	getTimestamp
CLOB	java.sql.Clob	setClob	getClob
BLOB	java.sql.Blob	setBlob	getBlob
ARRAY	java.sql.Array	setARRAY	getARRAY
REF	java.sql.Ref	SetRef	getRef
STRUCT	java.sql.Struct	SetStruct	getStruct

-- NULL --
避免使用返回原始数据类型的getXXX()方法。
对原始数据类型使用包装类，并使用ResultSet对象的wasNull()方法来测试接收getXXX()方法的返回值的包装器类变量是否应设置为null。
使用原始数据类型和ResultSet对象的wasNull()方法来测试接收到由getXXX()方法返回的值的原始变量是否应设置为表示NULL的可接受值。
下面是一个用来处理NULL值的例子 -

Statement stmt = conn.createStatement( );
String sql = "SELECT id, first, last, age FROM Employees";
ResultSet rs = stmt.executeQuery(sql);

int id = rs.getInt(1);
if( rs.wasNull( ) ) {
   id = 0;
}

-- SQLException --

驱动程序和数据库中都会发生SQLException。 发生这种异常时，SQLException类型的对象将被传递给catch子句。

传递的SQLException对象具有以下可用于检索有关异常信息的方法 -

方法	描述
getErrorCode( )	获取与异常关联的错误代码。
getMessage( )	获取驱动程序处理的错误的JDBC驱动程序的错误消息，或获取数据库错误的Oracle错误代码和消息。
getSQLState( )	获取XOPEN SQLstate字符串。 对于JDBC驱动程序错误，不会从此方法返回有用的信息。 对于数据库错误，返回五位数的XOPEN SQLstate代码。 此方法可以返回null。
getNextException( )	获取异常链中的下一个Exception对象。
printStackTrace( )	打印当前异常或可抛出的异常，并将其追溯到标准错误流。
printStackTrace(PrintStream s)	将此throwable及其回溯打印到指定的打印流。
printStackTrace(PrintWriter w)	打印这个throwable，它是回溯到指定的打印器(PrintWriter)。