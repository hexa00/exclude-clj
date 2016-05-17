# exclude-clj

A Clojure script to create a list of tests to ignore in GDB tests from an
racy.sum generated with make check RACY_ITER=3

## Usage

```
Exclude racy tests

Usage exclude-clj -r racy.sum -k keep.sum -o stable-tests

  -r, --racy FILE    racy          Racy tests in .sum format
  -k, --keep FILE                  Tests to always keep in .sum format
  -o, --output FILE  ignore-tests  Output filename
  -h, --help
```
## License

GPLv3