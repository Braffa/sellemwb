package setuptables;

import com.braffa.sellem.datafortesting.ProductTestData;
import com.braffa.sellem.datafortesting.RegisteredUserTestData;
import com.braffa.sellem.datafortesting.UserToProductTestData;
import com.braffa.sellem.model.sql.Product;
import com.braffa.sellem.model.sql.RegisteredUser;
import com.braffa.sellem.model.sql.UserToProduct;
import com.braffa.sellem.tables.IDBActions;
import com.braffa.sellem.tables.TableEnum;
import com.braffa.sellem.tables.TableFactory;

public class SetUpTables {

	private static IDBActions mySqlProduct;

	private ProductTestData ptd = new ProductTestData();

	private static IDBActions mySqlRegisteredUser;

	private RegisteredUserTestData rutd = new RegisteredUserTestData();

	private static IDBActions mySqlUserToProduct;

	private UserToProductTestData utptd = new UserToProductTestData();

	public void createTables() throws Exception {
		System.out.println("SetUpTables createTables");
		mySqlProduct = TableFactory.getTable(TableEnum.PRODUCT);
		mySqlRegisteredUser = TableFactory.getTable(TableEnum.REGISTERED_USER);
		mySqlUserToProduct = TableFactory.getTable(TableEnum.USER_TO_PRODUCT);
		try {
			mySqlProduct.dropTable();
			mySqlRegisteredUser.dropTable();
			mySqlUserToProduct.dropTable();
		} catch (Exception e) {
			// ignore if no table found
		}
		mySqlProduct.createTable();
		mySqlRegisteredUser.createTable();
		mySqlUserToProduct.createTable();
		
		System.out.println("SetUpTables set up product");

		Product product = ptd.setUpProduct1();
		mySqlProduct.create(product);
		product = ptd.setUpProduct2();
		mySqlProduct.create(product);
		product = ptd.setUpProduct3();
		mySqlProduct.create(product);
		product = ptd.setUpProduct4();
		mySqlProduct.create(product);
		product = ptd.setUpProduct5();
		mySqlProduct.create(product);
		product = ptd.setUpProduct6();
		mySqlProduct.create(product);
		
		System.out.println("SetUpTables set up registered user");

		RegisteredUser registeredUser = rutd.setUpRegisteredUser1();
		mySqlRegisteredUser.create(registeredUser);
		registeredUser = rutd.setUpRegisteredUser2();
		mySqlRegisteredUser.create(registeredUser);
		registeredUser = rutd.setUpRegisteredUser3();
		mySqlRegisteredUser.create(registeredUser);
		registeredUser = rutd.setUpRegisteredUser4();
		mySqlRegisteredUser.create(registeredUser);
		registeredUser = rutd.setUpRegisteredUser5();
		mySqlRegisteredUser.create(registeredUser);
		registeredUser = rutd.setUpRegisteredUser6();
		mySqlRegisteredUser.create(registeredUser);
		registeredUser = rutd.setUpRegisteredUser7();
		mySqlRegisteredUser.create(registeredUser);
		registeredUser = rutd.setUpRegisteredUser8();
		mySqlRegisteredUser.create(registeredUser);
		registeredUser = rutd.setUpRegisteredUser9();
		mySqlRegisteredUser.create(registeredUser);
		
		System.out.println("SetUpTables set up user to product");

		UserToProduct UserToProduct = utptd.insertUserToProductTable1();
		mySqlUserToProduct.create(UserToProduct);
		UserToProduct = utptd.insertUserToProductTable2();
		mySqlUserToProduct.create(UserToProduct);
		UserToProduct = utptd.insertUserToProductTable3();
		mySqlUserToProduct.create(UserToProduct);
		UserToProduct = utptd.insertUserToProductTable4();
		mySqlUserToProduct.create(UserToProduct);
		UserToProduct = utptd.insertUserToProductTable5();
		mySqlUserToProduct.create(UserToProduct);
		UserToProduct = utptd.insertUserToProductTable6();
		mySqlUserToProduct.create(UserToProduct);
		UserToProduct = utptd.insertUserToProductTable7();
		mySqlUserToProduct.create(UserToProduct);
		UserToProduct = utptd.insertUserToProductTable8();
		mySqlUserToProduct.create(UserToProduct);
		UserToProduct = utptd.insertUserToProductTable9();
		mySqlUserToProduct.create(UserToProduct);

	}

}
