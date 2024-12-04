<h1>Simplified PicPay</h1>


<h2>About the project</h2>
<p>
Simplified PicPay it is a simplified payment platform. In it, it is possible to make transactions between two users.
We have two types of users: common users and merchants, both have wallets with money and can realize transactions.
</p>


<h2>Business Rules</h2>
<p>
Both types of users have a first name, last name, document, email and password. The document must be unique 
in the system. Therefore, the system can only allow one registration with the same document or email;<br>
Users can only update their password;<br>
Common users can send money (make transactions) to merchants and to other users;<br>
Merchants can only receive money, they cannot send money;<br>
Validate if the user has the necessary balance to make a transaction before performing it;<br>
Create a MOCK API or an internal API to authorize the transaction.<br>
Create a MOCK API or an internal API to simulate sending an email. This API may be offline occasionally;<br>
Use your imagination to simulate the logic of the result return;<br>
Transactions must be reversed in any case of inconsistency, and the money shall be returned to the user who sent it;<br>
Users are only allowed to update their password;<br>
This service must be RESTful.
</p>


<h2>Data to test the system</h2>
<p>
[<br>
    {
        "firstName": "Raphael",<br>
        "lastName": "Carsh",<br>
        "document": "75292019735",<br>
        "email": "rapha@gmail.com",<br>
        "password": "raphaSenha",<br>
        "balance": 50.00,<br>
        "usuarioType": "COMMON"<br>
    },<br><br>
    {
        "firstName": "Lucas",<br>
        "lastName": "Silva",<br>
        "document": "40129592132",<br>
        "email": "lucas@gmail.com",<br>
        "password": "lucasSenha",<br>
        "balance": 310.00,<br>
        "usuarioType": "COMMON"<br>
    },<br><br>
    {
        "firstName": "Felipe",<br>
        "lastName": "Torkov",<br>
        "document": "31295212332314",<br>
        "email": "felipe@gmail.com",<br>
        "password": "felipeSenha",<br>
        "balance": 70.00,<br>
        "usuarioType": "MERCHANT"<br>
    }<br>
]
</p>
