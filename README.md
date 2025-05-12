# COMP421P3 - The JDBC Program of KABY 🚚

KABY is a fictional logistics company that provides global C2C delivery services. The company's structure is modelled on a generalised representation of delivery companies like DHL, UPS, etc. The name KABY is based on the first letter of each group member's name.

In the course COMP-421: Database Systems, Our group developed KABY over three projects. In this third and final project, one of the objectives was to produce a JDBC program that allows users to query KABY's SQL database. The available query options are:
1. Finding the delivery date of an order
2. Estimating the delivery time of an order
3. Adding a new order to the database
4. Updating the price of the goods stored in an order
5. Reassigning an employee to an order



## The Relational Schema

The relational schema defines the SQL tables of KABY. Around 300 realistic records were created for the project, with the records representing the worldwide logistical activities of KABY. A sample of the records can be provided upon request

### The Entities

Client (ClientID, Name, Email, Address, PhoneNumber, AmountPayed)
> Represents the individuals who are shipping parcels with KABY or are receiving parcels from KABY

Employee (EmployeeID, Name, Email, Role, Department)
> Stores the employee records of KABY

Goods (GoodsID, UnitPrice, Quantity, Mass, Description)
> Goods collected for shipments are categorised into representative products. Such information can be beneficial for customs declarations or for delivery feasability estimations

Order (OrderID, ClientID, GoodsID, EmployeeID, ProductQuantity, OrderSummary)
> Collect the delivery orders placed by clients. Each order is assigned to one employee; the order summary is described in the format: "Client A sent Goods to Client B, handled by Employee." This table references Client, Goods, and Employee

Transportation (RegistrationID, VehicleType, Speed, Capacity)
> Stores generalised representations of the vehicles KABY has for delivering parcels. The available vehicles are scooters vans, lorries, airplane, train, and boat

Route (RouteID, Origin, Destination, Distance)
> Represents the paths delivery vehicles can use to ship orders

Domestic_Route (RouteID, TollFees)
> For routes within a country, keep track of any toll fees. This table references Route

International_Route (RouteID, CustomsCapability)
> For routes travelling across countries, check if any border restrictions exist. A free trade zone would be classified as a restriction-free international route. This table references Route

Tracking (TrackingID, OrderID, CurrentLocation, EstimatedTimeOfArrival)
> Stores the tracking numbers of all orders in the database. This table references Order

Warehouse (Location, StockLevel, Capacity, OperatingHours)
> Lists the warehouses KABY uses for its delivery services

### The Relationships

Contains (OrderID, GoodsID)
> Determine what sort of goods are contained in an order. This table references Order and Goods

Ships (RegistrationID, OrderID)
> Identify the type of vehicle delivering an order. This table references Transportation and Order

Follows (RegistrationID, RouteID, OrderID)
> Understand the route a vehicle is using to deliver an order. This table references Transportation, Route, and Order

Stores (Location, GoodsID)
> Check on the goods are stored at each warehouse. This table references Warehouse and Goods
