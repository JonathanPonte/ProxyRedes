package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Utils {

	public static final String ARQUIVO = "SitesBloqueados.txt";

	public static String[] getBlokerUrls() {
		File file = new File(ARQUIVO);

		FileReader fr = null;
		BufferedReader br = null;

		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String linha;
			while ((linha = br.readLine()) != null) {
				String[] dados = linha.split(";");
				return dados;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

}
