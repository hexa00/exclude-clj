(ns exclude-clj.core
  (:require [clojure.string :as string]
            [clojure.set :as set]
            [clojure.tools.cli :refer [parse-opts]]))

(defn extract-test-from-racy [racy-line]
  "Extract the test path from the test line output"
  (apply str (drop-last 1 (re-find #"[^:]*:" racy-line))))

(defn extract-test-from-all [line]
  "Extract the test name from all test as taken by find . -iname '*.exp'"
  (apply str (drop 2 line)))

(defn read-sum-tests [filename]
  (->> (slurp filename)
       (string/split-lines)
       (map extract-test-from-racy)
       (set)))

(defn read-find-tests [filename]
  (->> (slurp filename)
       (string/split-lines)
       (map extract-test-from-all)
       (set)))

(def cli-options
  ;; options list
  [["-a" "--all FILE" "All test as find output"
    :default "all"
    ]
   ["-r" "--racy FILE" "Racy tests in .sum format"
    :default "racy"]
   ["-k" "--keep FILE" "Tests to always keep in .sum format"]
   ["-o" "--output FILE" "Output filename"
    :default "stable-tests"]
   ["-h" "--help"]]
  )

(defn exit [status msg]
  (println msg)
  (System/exit status)
  )

(defn usage [options-summary]
  ;; display usage
  (->>
   ["Exclude racy tests"
    ""
    "Usage exclude-clj -a all -r racy.sum -k keep.sum -o stable-tests"
    ""
    options-summary]
   (string/join \newline))
  )

(defn -main [& args]
  "Main "
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (usage summary))
      (not= (count arguments) 0) (exit 1 (usage summary)))
    (spit (:output options)
          (let [excluded-test (read-sum-tests (:racy options))
                all-tests (read-find-tests (:all options))
                keep-tests (cond (:keep options) (read-sum-tests (:keep options)))]
            (apply str (sort (map (fn [x] (str x "\n")) (set/union (set/difference all-tests excluded-test) keep-tests))))
            ))
    )
















  )
