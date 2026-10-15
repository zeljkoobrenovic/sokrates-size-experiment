"""Switch off the correlations section for the fixture: on these two repositories the baseline crashes in it
(a NullPointerException in FileChurnReportGenerator.addCorrelations), which stops the report before any HTML
page is written. Both the baseline and the new code run with this setting."""
import json, sys
path = sys.argv[1]
config = json.load(open(path))
config["analysis"]["skipCorrelations"] = True
json.dump(config, open(path, "w"), indent=2)
