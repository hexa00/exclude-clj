# exclude-clj

A Clojure script to create a list of stable GDB tests from an exclude list

## Usage

```
Exclude racy tests

Usage exclude-clj -a all -r racy.sum -k keep.sum -o stable-tests

  -a, --all FILE     all           All test as find output
  -r, --racy FILE    racy          Racy tests in .sum format
  -k, --keep FILE                  Tests to always keep in .sum format
  -o, --output FILE  stable-tests  Output filename
  -h, --help
```
## License

GPLv3