<h1>Simplified PicPay</h1>
> Status: DEVELOPMENT(front-end side) ⚠️


<h2>IMPORTANT</h2>
<p>
Go to application.properties and ensure that <code>spring.datasource.username=root</code> and
<code>spring.datasource.password=root</code> represent the correct username and password for your database.<br>
This project uses swagger documentation, to access it, open your browser and go
to: <a href="http://localhost:8080/swagger-ui/index.html">http://localhost:8080/swagger-ui/index.html</a> 
while the application is running. Make sure you're using the correct port, in this case it is "8080".
</p>

<h2>About the project</h2>
<p>
Simplified PicPay is a simplified payment platform. In it, it is possible to make transactions between two users.
We have two types of users: common users and merchants, both have wallets with money and can perform transactions.
</p>


<h2>Business Rules</h2>
<ul>
  <li>Both types of users have a first name, last name, document, email, and password. The document must be unique in the system. Therefore, the system only allows one registration with the same document or email;</li>
  <li>Users can only update their password;</li>
  <li>Common users can send money (make transactions) to merchants and other common users;</li>
  <li>Merchants can only receive money; they cannot send money;</li>
  <li>Validate if the user has the necessary balance to make a transaction before performing it;</li>
  <li>Create a MOCK API or an internal API to authorize the transaction;</li>
  <li>Create a MOCK API or an internal API to simulate sending an email. This API may occasionally be offline;</li>
  <li>Transactions must be reversed in case of any inconsistency, and the money should be returned to the user who sent it;</li>
  <li>This service must be RESTful.</li>
</ul>


<h2>What I Have Learned</h2>
<ul>
  <li><strong>Java Programming and Frameworks:</strong> Gained hands-on experience with Java, particularly focusing on advanced concepts such as dependency injection, annotations and Spring Boot framework.</li>
  <li><strong>RESTful API Development:</strong> Designed and implemented APIs following RESTful principles, including methods for creating, reading, updating, and deleting resources (CRUD).</li>
  <li><strong>Exception Handling:</strong> Implemented custom exception classes and centralized exception handling using <code>@ExceptionHandler</code>.</li>
  <li><strong>Database Design and Integration:</strong> Worked with relational databases using MySQL. Learned to map entities with JPA/Hibernate annotations and manage schema relationships.</li>
  <li><strong>Validation:</strong> Utilized Jakarta Validation (<code>@Valid</code>, <code>@NotNull</code>, and custom validation rules) to ensure data integrity and robust input handling for API endpoints.</li>
  <li><strong>Technical Documentation:</strong> Swagger to generate API documentation, improving understanding of API endpoints.</li>
  <li><strong>Unit and Integration Testing:</strong> Writing tests using JUnit 5 and Mockito, simulating service behaviors, and validating business logic and exception handling.</li>
  <li><strong>Dependency Injection:</strong> Applied the concept of dependency injection to manage components and services effectively within the Spring framework.</li>
  <li><strong>Security and Validation:</strong> Implemented measures to validate sensitive data, such as CNPJ and CPF, and ensured secure handling of user data during transactions.</li>
  <li><strong>Version Control and Collaboration:</strong> Applied version control strategies to manage project development and collaboration with Git.</li>
  <li><strong>Configuration Management:</strong> Created configuration files such as <code>application.properties</code> or <code>application.yml</code> to manage environment-specific settings.</li>
  <li><strong>Transactional Behavior:</strong> Worked with <code>@Transactional</code> annotations to ensure consistency and integrity during operations involving multiple database interactions.</li>
  <li><strong>Layered Architecture:</strong> Designed and implemented a clean architecture, separating concerns into controllers, services, and repositories for maintainability and scalability.</li>
  <li><strong>Error Simulation and Debugging:</strong> Debugged errors related to unused methods, repository injections, and exception triggering, refining problem-solving techniques and troubleshooting skills.</li>
  <li><strong>Third-party Tools:</strong> Utilized libraries like RestTemplate for external API communication.</li>
</ul>


<h2>Data to test the system</h2>
<p>
[<br>
    {
        "firstName": "Raphael",<br>
        "lastName": "Carsh",<br>
        "document": "75292019735",<br>
        "email": "rapha@gmail.com",<br>
        "password": "raphaPassword",<br>
        "balance": 90.00,<br>
        "userType": "COMMON"<br>
    },<br><br>
    {
        "firstName": "Lucas",<br>
        "lastName": "Silva",<br>
        "document": "40129592132",<br>
        "email": "lucas@gmail.com",<br>
        "password": "lucasPassword",<br>
        "balance": 310.00,<br>
        "userType": "COMMON"<br>
    },<br><br>
    {
        "firstName": "Felipe",<br>
        "lastName": "Torkov",<br>
        "document": "31295212332314",<br>
        "email": "felipe@gmail.com",<br>
        "password": "felipePassword",<br>
        "balance": 100.00,<br>
        "userType": "MERCHANT"<br>
    },<br>
    {
        "firstName": "Carlos",<br>
        "lastName": "Oliveira",<br>
        "document": "72682217902162",<br>
        "email": "carlos@gmail.com",<br>
        "password": "carlosPassword",<br>
        "balance": 175.00,<br>
        "userType": "MERCHANT"<br>
    }<br>
]
</p>
