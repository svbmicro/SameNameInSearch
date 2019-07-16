String sProjectName = "Microstrategy tutorial";



java.util.Map<String, ResultSet> aResultArray = new java.util.HashMap<String, ResultSet>();


java.util.Map<String, String> aNames = new java.util.HashMap<String, String>();


ResultSet oResult = executeCapture("LIST ALL ATTRIBUTES FOR PROJECT '" + sProjectName + "';");

aResultArray.put("Attribute", oResult);


ResultSet oMetrics = executeCapture("LIST ALL METRICS FOR PROJECT '" + sProjectName + "';");

aResultArray.put("Metric", oMetrics);

ResultSet oDocuments = executeCapture("LIST ALL DOCUMENTS FOR PROJECT '" + sProjectName + "';");

aResultArray.put("Document", oDocuments);

ResultSet oReports = executeCapture("LIST ALL Reports FOR PROJECT '" + sProjectName + "';");

aResultArray.put("Grid", oReports);

for (String sKey : aResultArray.keySet()) {


	ResultSet RSet = aResultArray.get(sKey);

	printOut(sKey);
	RSet.moveFirst();
	while (!RSet.isEof()) {
		String sObjectName = (String) RSet.getFieldValueString(DisplayPropertyEnum.NAME);
		String sObjectHide = (String) RSet.getFieldValueString(DisplayPropertyEnum.HIDDEN);
		String sObjectPath = "";



		if (sKey.contains("Grid") || sKey.contains("Document")) {
			sObjectPath = (String) RSet.getFieldValueString(DisplayPropertyEnum.FOLDER);
		} else {
			sObjectPath = (String) RSet.getFieldValueString(DisplayPropertyEnum.PATH);
		}


		if (sObjectHide.contains("True") || sObjectPath.contains("\\Profiles")
				|| sObjectPath.contains("\\Object Templates")){

			RSet.moveNext();
			continue;
		}

		if (sObjectHide.contains("False") && !sObjectPath.contains("\\Profiles") && !sObjectPath.contains("\\Object Templates") && aNames.containsKey(sObjectName)) {


			if (aNames.get(sObjectName) != "") {

				printOut(sKey + " :" + sObjectName + " - " + aNames.get(sObjectName));
			}

			printOut(sKey + " :" + sObjectName + " -  " + sObjectPath);

			aNames.put(sObjectName, "");

		} else {
			aNames.put(sObjectName, sObjectPath);
		}

		RSet.moveNext();

	}

	aNames.clear();
}

