(ns exclude-clj.core
  (:require [clojure.string :as string]
            [clojure.set :as set]
            [clojure.tools.cli :refer [parse-opts]]))

(defn extract-test-from-racy [racy-line]
  "Extract the test path from the test line output"
  (apply str (drop-last 1 (last (re-find #"([^/]*/)([^:]*:)" racy-line)))))

(defn read-sum-tests [filename]
  (->> (slurp filename)
       (string/split-lines)
       (map extract-test-from-racy)
       (set)))

(def cli-options
  ;; options list
  [["-r" "--racy FILE" "Racy tests in .sum format"
    :default "racy"]
   ["-k" "--keep FILE" "Tests to always keep in .sum format"]
   ["-o" "--output FILE" "Output filename"
    :default "ignore-tests"]
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
    "Usage exclude-clj -r racy.sum -k keep.sum -o ignore-tests"
    ""
    options-summary]
   (string/join \newline))
  )

;;; fixme build an ignore list for use with --ignore testname
;;(testname is without it's path)
(defn -main [& args]
  "Main "
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (usage summary))
      (not= (count arguments) 0) (exit 1 (usage summary)))
    (spit (:output options)
          (let [excluded-test (read-sum-tests (:racy options))
                keep-tests (cond (:keep options) (read-sum-tests (:keep options)))]
            (apply str (sort (map (fn [x] (str x " ")) (set/difference excluded-test keep-tests))))
            ))
    )
















  )
