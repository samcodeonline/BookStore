import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Scanner;


class Book implements Serializable {
    int BookId;
    String BookName;
    String BookUsername;

    Book(int BookId, String BookName, String BookUsername){
        this.BookId = BookId;
        this.BookName = BookName;
        this.BookUsername = BookUsername;
    }
    public String toString(){
        return BookId + " " + BookName + " " + BookUsername;
    }
}

public class Main extends  {
    static void menu() {
        System.out.println("1. insert \n" + "2. Update \n" + "3. Delete \n" + "4. Exit Program" );
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/books";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password); Statement stmt = connection.createStatement();) {
            String sql = "CREATE TABLE bookregistration " + "id INT AUTO_INCREMENT , " + " bookName VARCHAR(55), " + " authorName VARCHAR(55), " + " PRIMARY KEY ( id )";

            System.out.println("Created table in given database...");

            Scanner intTypes = new Scanner(System.in);
            Scanner stringTypes = new Scanner(System.in);
            while (true) {
                menu();
                int choose = intTypes.nextInt();
                switch (choose) {
                    case 1:
                        //                    insert using database
                        System.out.print("Book Name : ");
                        String book_name = stringTypes.nextLine();
                        System.out.print("Author Name : ");
                        String author_name = stringTypes.nextLine();
                        String insertionQuery = "INSERT INTO bookregistration (bookName, authorName) VALUES  (?,?)";
                        PreparedStatement insertStatment = connection.prepareStatement(insertionQuery);
                        insertStatment.setString(1, book_name);
                        insertStatment.setString(2, author_name);
                        insertStatment.executeUpdate();
                        System.out.println("Record inserted");
                        break;

                    case 2:
                        System.out.print("Tell the Book id: ");
                        int book_update_id = intTypes.nextInt();
                        System.out.print("Book Name : ");
                        String update_book_name = stringTypes.nextLine();
                        System.out.print("Author Name : ");
                        String update_author_name = stringTypes.nextLine();
                        String updateQuery = "UPDATE bookregistration SET bookName = " + update_book_name + "," + " authorName = " + update_author_name + " WHERE id = " + book_update_id;
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setString(1, update_book_name);
                        updateStatement.setString(2, update_author_name);
                        updateStatement.executeUpdate();
                        System.out.println("Record updated.");
                        break;
                    case 3:
                        // DELETE a record
                        System.out.print("Tell the Book id: ");
                        int book_delete_id = intTypes.nextInt();
                        String deleteQuery = "DELETE FROM bookregistration WHERE id = " + book_delete_id;
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                        deleteStatement.executeUpdate();
                        System.out.println("Record deleted.");
                        break;
                    case 4:
                    case 3:
                    if(true){
                        boolean found = false;
                        System.out.println("--------------BookSearchedData--------------");
                        System.out.print("Enter Book Author Name : ");
                        while (!found) {
//                            Book Book = (Book) list_iterator.next();
                            if (Book.BookUsername.contains(BookAuthor)){
                                System.out.println(Book);
                                found = true;
                            System.out.println("-----------------------------------");
                            }
                        } if(!found) {
                            System.out.println("Book not Found");
                        }
                    }
                    else {
                        System.out.println("File not Found");
                    }
                    break;
                    case 5:
                        System.out.println("Exit");
                        return;
                }

            }
            //            Record Insertion
//            String insertionQuery = "INSERT INTO `bookregistration`(`bookName`, `authorName`) VALUES (?,?)";
//            PreparedStatement insertStatment = connection.prepareStatement(insertionQuery);
//
//            insertStatment.setString(1, "The 48 Laws of Power");
//            insertStatment.setString(2, "Robert Greene");
//
//            insertStatment.setString(1, "Focus");
//            insertStatment.setString(2, "Charles Duhigg");
//
//            insertStatment.setString(1, "Evolution in Humans");
//            insertStatment.setString(2, "Charles Darwin");
//
//            insertStatment.setString(1, "Evolution in Humans");
//            insertStatment.setString(2, "Charles Darwin");
//
//            insertStatment.setString(1, "Rich Dad, Poor Dad");
//            insertStatment.setString(2, "Robert T Kiyosaki");
//
//            insertStatment.setString(1, "Focus");
//            insertStatment.setString(2, "Charles Duhigg");
//
//            insertStatment.executeUpdate();
//            System.out.println("Record created.");
//
//            // UPDATE a record
//            String updateQuery = "UPDATE bookregistration SET bookName = ? , authorName = ? WHERE id = 46";
//            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
//            updateStatement.setString(1, "The 48 PERSONS");
//            updateStatement.setString(2, "Roger Delores");
//            updateStatement.executeUpdate();
//            System.out.println("Record updated.");
//
//            // DELETE a record
//            String deleteQuery = "DELETE FROM bookregistration WHERE bookName = 'Rich Dad, Poor Dad' ";
//            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
//            deleteStatement.executeUpdate();
//            System.out.println("Record deleted.");
//        }
        } catch (Exception e) {
            System.out.println(e);
        }
//        try{
//            Class.forName("com.mysql.cj.jdbc.Driver");
//
//            Connection connection = DriverManager.getConnection(url,username,password);
//
//            Statement statement = connection.createStatement();
//
//            ResultSet resulSet = statement.executeQuery("select * from book");
//
//
//
////            Record Insertion
//
//            String insertionQuery = "INSERT INTO `book`(`id`, `name`, `user_name`) VALUES (?,?,?)";
//            PreparedStatement insertStatment = connection.prepareStatement(insertionQuery);
//            insertStatment.setInt(1, 12);
//            insertStatment.setString(2, "The 48 Laws of Power");
//            insertStatment.setString(3, "Robert Greene");
//            insertStatment.executeUpdate();
//            System.out.println("Record created.");
//
////`         Update a record
//
//            String updateQuery = "UPDATE `book` SET `id`=?,`name`=?,`user_name`=? WHERE 1";
//            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
//            updateStatement.setInt(1, 24);
//            updateStatement.setString(2, "The 48 Laws ");
//            updateStatement.setString(3, "Robert Green");
//            updateStatement.executeUpdate();
//            System.out.println("Record updated.");
//
////          Delete a record
//
//            String sql = "DELETE FROM `book` WHERE ?";
//            PreparedStatement deleteStatement = connection.prepareStatement(sql);
//            deleteStatement.setInt(1, 24);
//            deleteStatement.executeUpdate();
//            System.out.println("Record deleted.");
//
//            String insertionQuerie = "INSERT INTO `book`(`id`, `name`, `user_name`) VALUES (?,?,?)";
//            PreparedStatement insertStat = connection.prepareStatement(insertionQuerie);
//            insertStat.setInt(1, 12);
//            insertStat.setString(2, "The 48 Laws of Power");
//            insertStat.setString(3, "Robert Greene");
//            insertStat.executeUpdate();
//            System.out.println("Record created.");
//
//            while(resulSet.next()){
//                System.out.println(resulSet.getInt(1) + " " + resulSet.getString(2) + " --By-- " + resulSet.getString(3));
//            }
//            connection.close();
//        }catch (SQLException e){
//            System.out.println(e);
//        }

//        Create  a new object

//        try {
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        int choice = -1;
//        Scanner scanIntTypes  = new Scanner(System.in);
//        Scanner scanStringTypes = new Scanner(System.in);
//        File BookFile = new File("Book.txt");
//        ArrayList<Book> array_list = new ArrayList<Book>();
//        ObjectOutputStream object_output_stream = null;
//        ObjectInputStream object_input_stream = null;
//        ListIterator list_iterator = null;
//
//        if (BookFile.isFile()) {
//            object_input_stream = new ObjectInputStream(new FileInputStream(BookFile));
//            array_list = (ArrayList<Book>) object_input_stream.readObject();
//            object_input_stream.close();
//        }

//        do{
//            System.out.println("---------------------MyLibrary-------------------");
//            System.out.println("1. Insert Books ");
//            System.out.println("2. Display Books");
//            System.out.println("3. Search Books ");
//            /*Search by Strings and subStrings of the string
//            1. Searching in string
//            2. Searching via String inside string
//            3. Searching in string
//            */
//            System.out.println("4. Delete Books ");
//            System.out.println("0. Exit File");
//            System.out.print("Enter Your Choice: ");
//            choice = scanIntTypes.nextInt();
//            switch(choice){
//                case 1:
//                    System.out.println("-------------InsertData---------------");
//                    System.out.print ("Enter totalNumber of Book: ");
//
//                    int BookCount = scanIntTypes.nextInt();
//
//                    for(int i = 0 ; i < BookCount ; i++){
//
//                        System.out.print ("SerialNo: ");
//                        int BookId = scanIntTypes.nextInt();
//                        System.out.print ("Enter Book Name: ");
//                        String BookName =  scanStringTypes.nextLine();
//                        System.out.print ("Enter Book Author: ");
//                        String BookUsername = scanStringTypes.nextLine();
//                        array_list.add(new Book(BookId, ". " + BookName ,  "By " + BookUsername));
//                    }
//                    object_output_stream = new ObjectOutputStream(new FileOutputStream(BookFile));
//                    object_output_stream.writeObject(array_list);
//                    object_output_stream.close();
//                    break;
//                case 2:
//                    if(BookFile.isFile()) {
//                        object_input_stream = new ObjectInputStream(new FileInputStream(BookFile));
//                        array_list = (ArrayList<Book>) object_input_stream.readObject();
//                        object_input_stream.close();
//                        list_iterator = array_list.listIterator();
//                        if(!list_iterator.hasNext()){
//                            System.out.println("there is nothing in it");
//                        }
//                        else{
//                            System.out.println("--------------UserData--------------");
//                            while (list_iterator.hasNext()) {
//                                System.out.println(list_iterator.next());
//                                System.out.println("--------------------------------");
//                            }
//                        }
//                    }  else{
//                        System.out.println("File Not Found");
//                    }
//                    break;
//                case 3:
//                    if(BookFile.isFile()) {
//                        object_input_stream = new ObjectInputStream(new FileInputStream(BookFile));
//                        array_list = (ArrayList<Book>) object_input_stream.readObject();
//                        object_input_stream.close();
//                        boolean found = false;
//                        System.out.println("--------------BookSearchedData--------------");
//                        System.out.print("Enter Book Author Name : ");
//                        String BookAuthor = scanStringTypes.nextLine();
//                        list_iterator = array_list.listIterator();
//                        while (list_iterator.hasNext()) {
//                            Book Book = (Book) list_iterator.next();
//                            if (Book.BookUsername.contains(BookAuthor)){
//                                System.out.println(Book);
//                                found = true;
//                            System.out.println("-----------------------------------");
//                            }
//                        } if(!found) {
//                            System.out.println("Book not Found");
//                        }
//                    }
//                    else {
//                        System.out.println("File not Found");
//                    }
//                    break;
//                case 4:
//                    if(BookFile.isFile()) {
//                        object_input_stream = new ObjectInputStream(new FileInputStream(BookFile));
//                        array_list = (ArrayList<Book>) object_input_stream.readObject();
//                        object_input_stream.close();
//                        Boolean found = false;
//                        System.out.println("--------------BookSearchedData--------------");
//                        list_iterator = array_list.listIterator();
//                        System.out.print("Enter Book SerialNo to Delete : ");
//                        int BookId = scanIntTypes.nextInt();
//                        while (list_iterator.hasNext()) {
//                            Book Book = (Book) list_iterator.next();
//                            if (Book.BookId == BookId) {
//                                list_iterator.remove();
//                                found = true;
//                            }
////                            System.out.println("-----------------------------------");
//                        } if(found) {
//                            object_output_stream = new ObjectOutputStream(new FileOutputStream(BookFile));
//                            object_output_stream.writeObject(array_list);
//                            object_output_stream.close();
//                            System.out.println("Book Deleted SuccessFully");
//                        }
//                        else{
//                            System.out.println("Book not Found");
//                        }
//                        System.out.println("-----------------------------------------");
//                    }
//                    else {
//                        System.out.println("File not Found");
//                    }
//                    break;
////                case 5:
////                    if(BookFile.isFile()) {
////                        object_input_stream = new ObjectInputStream(new FileInputStream(BookFile));
////                        array_list = (ArrayList<Book>) object_input_stream.readObject();
////                        object_input_stream.close();
////                        Boolean found = false;
////                        System.out.println("--------------BookSearchedData--------------");
////                        list_iterator = array_list.listIterator();
////                        System.out.print("Enter Book SerialNo to update : ");
////                        int BookId = scanIntTypes.nextInt();
////                        while (list_iterator.hasNext()) {
////                            Book Book = (Book) list_iterator.next();
////                            if (Book.BookId == BookId) {
////                                System.out.println("Enter Book Name: ");
////                                String BookName = scanStringTypes.nextLine();
////                                System.out.println("Enter Book Author: ");
////                                String BookUserName = scanStringTypes.nextLine();
////                                list_iterator.set(new Book(BookId,BookName,BookUserName));
////                                found = true;
////                            }
////                            System.out.println("-----------------------------------");
////                        } if(found) {
////                            object_output_stream = new ObjectOutputStream(new FileOutputStream(BookFile));
////                            object_output_stream.writeObject(array_list);
////                            object_output_stream.close();
////                            System.out.println("Book Updated SuccessFully");
////                        }
////                        else{
////                            System.out.println("Book not Found");
////                        }
////                        System.out.println("-----------------------------------------");
////                    }
////                    else {
////                        System.out.println("File not Found");
////                    }
////                    break;
//            }
//        }while(choice != 0);
    }
}