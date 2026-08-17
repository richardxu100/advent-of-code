(ns advent-of-code-2025.day6.day6-improved 
  (:require
    [clojure.string :as str]
    [advent-of-code.utils.utils :as utils]))

(def input "./src/advent_of_code_2025/day6/input.txt")
(def test-input "./src/advent_of_code_2025/day6/test_input.txt")

(defn vertical-numbers [input]
  (->> input
       slurp
       (str/split-lines)
       butlast                      ; drop last line, with the operators
       utils/transpose
       (map (comp parse-long str/trim str/join))
       (partition-by nil?)
       (take-nth 2))); this takes every other element in the sequence

(vertical-numbers test-input)

(defn calculate [operators numbers]
  (->> (map (fn [op nums] (apply op nums)) operators numbers)
       (reduce +)))

(calculate [+ *] [[2 2 1 2] [3 12 9 2]]) ;; interesting thing here, is you can pass in the math operators as functions

(->> test-input
     slurp
     (str/split-lines)
     (map #(re-seq #"\d+|\*|\+" %)) ; re-seq is a way to apply a regexp on a sequence. I'm parsing the digits and operators
     last
     (mapv {"+" + , "*" *})
     )

(defn solve [input]
  (let [lines (->> input
                   slurp
                   str/split-lines
                   (map #(re-seq #"\d+|\*|\+" %)))
        operators (mapv {"+" + , "*" *} (last lines))
        numbers (vertical-numbers input)]
    (calculate operators numbers)))

(solve test-input)
(solve input)
