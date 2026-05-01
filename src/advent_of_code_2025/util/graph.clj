(ns advent-of-code-2025.util.graph
  (:require [clojure.string :as str]))

(defn parse-graph [input]
  (->> input
       slurp
       str/split-lines
       (map #(str/split % #""))))

