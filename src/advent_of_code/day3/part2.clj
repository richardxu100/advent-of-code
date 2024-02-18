(ns advent-of-code.day3.part2
  (:require [advent-of-code.day3.day3 :as day3]
            [advent-of-code.utils.utils :as utils]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.spec.alpha :as s]))

(def graph-input [["3" "*" "."]
                  ["4" "5" "."]
                  ["." "." "5"]
                  ["." "1" "5"]])


(defn parse-numbers-with-coords
  "docstring"
  [graph]
  (let [formatted-graph (map #(apply str %) graph)]
    (flatten (map-indexed (fn [y row] (let [results (utils/re-pos #"\d+" row)]
                                        (map (fn [[x number]] {:coord [x y] :val (Integer/parseInt number)}) results)
                                        )) formatted-graph)))
  )

(parse-numbers-with-coords graph-input)


(defn get-coords-number-takes-up [number-with-coords]
  (let [[x y] (:coord number-with-coords)
        num-length (count (str (:val number-with-coords)))]
    (set (map (fn [index] (list (+ x index) y)) (range num-length))))
  )


(defn is-in-neighbors? [neighbors number-with-coords]
  (if (seq (set/intersection (set neighbors) (get-coords-number-takes-up number-with-coords)))
    true
    false))

;(is-in-neighbors? [[0 0]] {:coord [1 0], :val 3})

(defn get-number-neighbors [x y numbers-with-coords]
  (let [neighbors (day3/find-neighbor-indices x y)]
    (do
      (println "neighbors: " neighbors)
      (println "numbers-with-coords: " numbers-with-coords)
      (println "filtered-numbers-with-coords: " (filter #(is-in-neighbors? neighbors %) numbers-with-coords))
      (map :val (filter #(is-in-neighbors? neighbors %) numbers-with-coords)))
    ))

;; I need to use number coords (so add some more spaces, for numbers that are more than one char. More complex neighbors logic)
;
(get-number-neighbors 1 0 (parse-numbers-with-coords graph-input))

(defn is-gear-symbol? [point]
  (= point "*"))

(defn get-gear-ratios [graph]
  (for [x (range (count (first graph)))
        y (range (count graph))
        :let [point (nth (nth graph y) x)
              numbers-with-coords (parse-numbers-with-coords graph)
              number-neighbors (get-number-neighbors x y numbers-with-coords)]
        :when (and (is-gear-symbol? point)
                   (= (count number-neighbors) 2))]
    (apply * number-neighbors)))

(defn sum-of-gear-ratios
  "docstring"
  [graph]
  (apply + (get-gear-ratios graph)))

