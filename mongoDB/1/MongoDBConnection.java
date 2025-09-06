import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;


public class MongoDBConnection {


    // Variáveis de configuração
    private static final String USERNAME = "";
    private static final String PASSWORD = "";
    private static final String CLUSTER_URL = ""; // Substitua pelo seu cluster se for diferente
    private static final String DATABASE_NAME = ""; // Substitua pelo nome do seu banco de dados


    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDBConnection() {
        try {
            // String de conexão com credenciais
            String connectionString = String.format("mongodb+srv://" + USERNAME + ":" + PASSWORD + "@" + CLUSTER_URL + "/?retryWrites=true&w=majority&appName=" + DATABASE_NAME);


            // Configuração do cliente MongoDB
            ConnectionString connString = new ConnectionString(connectionString);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .build();


            // Criando o cliente
            mongoClient = MongoClients.create(settings);


            // Selecionando o banco de dados
            database = mongoClient.getDatabase(DATABASE_NAME);


            System.out.println("Conexão estabelecida com sucesso ao MongoDB!");
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public MongoDatabase getDatabase() {
        return database;
    }


    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexão encerrada com sucesso.");
        }
    }


    public static void main(String[] args) {
        MongoDBConnection connection = new MongoDBConnection();
        UsuarioOperations ops = new UsuarioOperations(connection.getDatabase(), "Usuario");


        // Exemplo de uso
        if (connection.getDatabase() != null) {
            System.out.println("Banco de dados: " + connection.getDatabase().getName());
        }

        performOperations(ops);

        try {
            // Aguarde para garantir que os processos internos sejam concluídos
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        connection.closeConnection();
    }

    private static void performOperations(UsuarioOperations ops) {

        var listUsuarios = List.of(
            new Usuario("Alice", 25),
            new Usuario("Bob", 30),
            new Usuario("Charlie", 35)
        );

        listUsuarios.forEach(ops::create);

        System.out.println(ops.findAll());

        ops.upadteAgeByName("Bob", 32);

        System.out.println(ops.findAll());

        ops.deleteByName("Charlie");

        System.out.println(ops.findAll());

        ops.dropCollection();
    }
}