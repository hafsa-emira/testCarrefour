package lansrod.com.carrefour;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App {
	// private Logger logger = Logger.getLogger(App.class);
	private static Random random = new Random();
	private static int nbStores = 1200;
	private static FileWriter filewriter = null;
	private static Map<Integer, String> listStores = null;
	static CreateDate createDate = new CreateDate();
	private static Map<String, Map<String, Integer>> calculator = null;

	public static void main(String[] args) throws IOException {

		File file;
		String[] dates = createDate.createDates();
		listStores = new HashMap<Integer, String>();
		for (String date : dates) {
			for (int i = 1; i <= nbStores; i++) {
				CreateRandom createRandom = new CreateRandom();
				UUID storeId = createRandom.createStoreId();
				createReferenceProdFile(storeId, date);
				listStores.put(i, storeId.toString());
			}
			createTransactionsFile(date);
			Map<String, Map<String, Integer>> mostSelledProducts = mostSellersProducts(date);
			System.out.println(mostSelledProducts);
		}

	}

	// gerenate ReferenceProdFiles
	public static void createReferenceProdFile(UUID storeId, String day) throws IOException {
		String line = "";
		filewriter = new FileWriter("reference_prod-" + storeId + "_" + day + ".data");

		for (int j = 0; j < 100; j++) {
			// write product reference: product | price
			line = Integer.toString(j + 1) + "|" + Float.toString(new Random().nextFloat() * 100) + "\n";
			filewriter.write(line);

		}

		filewriter.close();
	}

	// gerenate TransactionsFiles
	public static void createTransactionsFile(String day) throws IOException {
		String line = "";
		// File file = new File("transactions-" + day + ".data");
		filewriter = new FileWriter("transactions-" + day + ".data");
		for (int j = 0; j < 200000; j++) {
			int repeatTransaction = random.nextInt(7);
			// generate transactions
			for (long i = 0; i < repeatTransaction; i++) {
				int productId = random.nextInt(100);
				int qte = random.nextInt(20);
				String storeId = listStores.get(random.nextInt(1200));
				line = j + "|" + day + "|" + storeId + "|" + productId + "|" + qte + "\n";
				filewriter.write(line);

			}
		}
		filewriter.close();
		// return file;
	}

	// generate top 100 selled products
	public static Map<String, Map<String, Integer>> mostSellersProducts(String day) throws IOException {

		String fileName = "transactions-" + day + ".data";
		calculator = new HashMap<String, Map<String, Integer>>();
		// read file into stream, try-with-resources

		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			stream.forEach(line -> {

				String idStore = line.split("\\|")[2];
				String product = line.split("\\|")[3];
				String qte = line.split("\\|")[4];
				int qteProduct = Integer.parseInt(qte);

				if (calculator.containsKey(idStore)) {
					if (calculator.get(idStore).containsKey(product)) {
						calculator.get(idStore).put(product, calculator.get(idStore).get(product) + qteProduct);
					} else {
						calculator.get(idStore).put(product, qteProduct);
					}
				} else {
					Map<String, Integer> calculatorValue = new HashMap<String, Integer>();
					calculatorValue.put(product, qteProduct);
					calculator.put(idStore, calculatorValue);

				}
			});
			// a Map with string keys and integer values
			for (Map.Entry<String, Map<String, Integer>> element : calculator.entrySet()) {
				Map<String, Integer> products = element.getValue();
				products = products.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
						.limit(100).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
								(oldValue, newValue) -> oldValue, LinkedHashMap::new));

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return calculator;

	}

}
