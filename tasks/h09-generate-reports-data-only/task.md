Add a `-dataOnly` option to the `generateReports` command. Landscapes and other tooling only read the analysis data, so for those uses generating the full HTML report set is wasted time and disk space.

With `-dataOnly`, the analysis runs as usual and the reports folder gets the analysis data exactly as today (the `data/` folder with `analysisResults.json`, `config.json`, the `text/` exports and the rest), but nothing else: no HTML reports or visuals (`html/`), no explorers (`explorers/`) and no source-code cache (`src/`). Log a line saying that only the data is stored.

Without the option nothing changes, and a later run without `-dataOnly` on the same output folder produces the full report set again. The option should show up in the usage/help of `generateReports` with an "[OPTIONAL]" description like the other options.
