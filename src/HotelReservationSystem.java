// this is my first project about hotel reservation system... on jdbc

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;
public class HotelReservationSystem {
    private static final String url="jdbc:mysql://127.0.0.1:3306/hotel_db";
private static final String username= "root";
private static final String password="Sadhan66";

    public static void main(String[] args) throws ClassNotFoundException {
try{
    Class.forName("com.mysql.cj.jdbc.Driver");
    }catch (ClassNotFoundException e){
    System.out.println(e.getMessage());
    }
try{
    Connection connection=DriverManager.getConnection(url,username,password);
while (true){
    System.out.println();
    System.out.println("HOTEL MANAGEMENT SYSTEM ");
    Scanner scanner=new Scanner (System.in);
    System.out.println("1. Reserve a Room ");
    System.out.println("2. View Reservation ");
    System.out.println("3. Get Room Number ");
    System.out.println("4. Update Reservation ");
    System.out.println("5. Delete Reservation ");
    System.out.println("0. Exit ");
    System.out.println(" PLEASE CHOOSE ANY OPTION GIVEN HERE ");
    int choice = scanner.nextInt();
    switch (choice){
        case 1:
            reserveRoom(connection,scanner);
            break;
        case 2:
            viewReservation(connection);
            break;
        case 3:
            getRoomNumber(connection,scanner);
        break;
            case 4:
                updateReservation(connection,scanner);
                break;
        case 5:
            deleteReservation(connection,scanner);
            break;
        case 0:
            exit();
            scanner.close();
            return;
        default:
            System.out.println("Invalid Choice. Try Again. ");

    }
}

} catch (SQLException e) {
    System.out.println(e.getMessage());
}  catch(InterruptedException e){
    System.out.println(e.getMessage());
}
    } // class main ends here ...
    private static void reserveRoom(Connection connection, Scanner scanner){
        try{
            System.out.println("Enter the Guest Name Please : ");
            scanner.nextLine();
            String guestName=scanner.nextLine();

            System.out.println("Enter the Room Number : ");
            int roomNumber= scanner.nextInt();
            System.out.println("Enter The Contact Number : ");
            String contactNumber = scanner.next();
            String sql ="INSERT INTO reservations(guest_name, room_number, contact_number) "+"VALUES('"+ guestName +"', "+ roomNumber +",'" + contactNumber +"' )";
       try(Statement statement= connection.createStatement()){
           int affectedRows =statement.executeUpdate(sql);
           if (affectedRows>0){
               System.out.println("Reservation Successful !!  ");
           } else {
               System.out.println("Reservation Failed !!");
           }
       }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void viewReservation(Connection connection) throws SQLException{
        String sql= "SELECT reservation_id ,guest_name, room_number,  contact_number, reservation_date FROM reservations" ;
   try (Statement statement =connection.createStatement();
   ResultSet resultSet= statement.executeQuery(sql)){
       System.out.println();
       System.out.println();
       System.out.println();
       System.out.println("Current Reservations : ");
       System.out.println("+..................+...................+................+.....................+............................+");
       System.out.println("|   Reservation Id |      Guest        |    Room Number |    Contact Number   |     Reservation Date      |");
       System.out.println("+..................+...................+................+.....................+.............................+");

       while (resultSet.next()){
     int reservationId = resultSet.getInt("reservation_id");
     String guestName= resultSet.getString("guest_name");
     int roomNumber= resultSet.getInt("room_number");
     String contactNumber= resultSet.getString("contact_number");
     String reservationDate = resultSet.getTimestamp("reservation_date").toString();
     System.out.printf ("|  %-14d |    %-15s |     %-13d | %-20s | %-19s |\n",
             reservationId, guestName, roomNumber,contactNumber,reservationDate  );
 }
       System.out.println("+..................+...................+................+.....................+...........................+");
   }
    }
    private  static void getRoomNumber(Connection connection, Scanner scanner){
        try {
            System.out.println("Enter the Reservation Id : ");
            int reservationId= scanner.nextInt();
            System.out.println("Enter the Guest Name : ");
            scanner.nextLine();
            String guestName =scanner.nextLine();
            String sql= "SELECT room_number FROM reservations WHERE reservation_id= "+ reservationId + " AND guest_name = '" +guestName+ "'";
     try (Statement statement= connection.createStatement();
     ResultSet resultSet=statement.executeQuery(sql)){
         if(resultSet.next()){
             int roomNumber= resultSet.getInt("room_number");
             System.out.println("Room number for Reservation Id "+ reservationId+ " and Guest "+ guestName+ "is "+ roomNumber);
         } else {
             System.out.println("Reservation Not Found For this given Id and Guest Name .. SORRY!!!");
                      }
     }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void updateReservation(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter Reservation Id to Update : ");
            int reservationId = scanner.nextInt();
            scanner.nextLine(); // consumes the next line character .
            if (!reservationExists(connection, reservationId)) {
                System.out.println(" Reservation Not found for the given Id ");
                return;
            }
                System.out.println("Enter new guest name :  ");
                String newGuestName = scanner.nextLine();
                System.out.println("Enter the Room number : ");
                int newRoomNumber = scanner.nextInt();
                System.out.println("Enter the new Contact Number  : ");
                String newContactNumber = scanner.next();
                String sql = "UPDATE reservations SET guest_name= '" + newGuestName + "', " + " room_number = " + newRoomNumber + " , " + " " +
                        "contact_number ='" + newContactNumber + "' " +
                        "WHERE reservation_id=" + reservationId;
                try (Statement statement = connection.createStatement()) {
                    int affectedRows = statement.executeUpdate(sql);
                    if (affectedRows > 0) {
                        System.out.println("Reservation Updated successfully !! ");
                    } else {
                        System.out.println("Updation Failed  ");
                    }
                }
            }catch (SQLException e){
            e.printStackTrace();
        }
        }
private static void deleteReservation(Connection connection , Scanner scanner){
            try {
                System.out.println(" Enter Reservation Id to delete : ");
                int reservationId= scanner.nextInt();
                if(!reservationExists(connection,reservationId )){
                    System.out.println("Reservation not Found for the given Id");
                    return ;
                }
                String sql= "DELETE FROM reservations WHERE reservation_id = "+reservationId;
                try (Statement statement= connection.createStatement()){
                    int affectedRows= statement.executeUpdate(sql);
                    if (affectedRows>0){
                        System.out.println("Deleted Successfully ");
                    }
                    else{
                        System.out.println(" Deletion Failed ");
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        private static boolean reservationExists(Connection connection, int reservationId){
        try{
            String sql= "SELECT reservation_id FROM reservations WHERE reservation_id ="+ reservationId;
            try(Statement statement= connection.createStatement();
            ResultSet resultSet= statement.executeQuery(sql)){
                return resultSet.next();
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        }
        private static void exit()throws InterruptedException{
            System.out.print("Exiting System ");
            for (int i=0; i<=4; i++){
                  System.out.print(".");
                Thread.sleep(450);
            }
            System.out.println();
            System.out.println("Thank you For Using This Reservation System !! ");
        }
    }
