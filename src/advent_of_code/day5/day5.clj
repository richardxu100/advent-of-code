(ns advent-of-code.day5.day5
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))

(defn in-range? [seed source-start range]
  (if (< seed source-start)
    false
    (>= (+ source-start range) seed)))

(defn calc-destination [seed entries]
  (loop [remaining-entries entries
         current-entry (first entries)]
    (cond
      (empty? remaining-entries)
      seed
      (in-range? seed (:source-start current-entry) (:range current-entry))
      (let [offset (- (:destination-start current-entry) (:source-start current-entry))]
        (+ seed offset))
      :else
      (let [new-remaining-entries (rest remaining-entries)
            new-current-entry (first new-remaining-entries)]
        (recur new-remaining-entries new-current-entry)))
    )
  )

(defn calc-location-for-seed [seed maps]
  (loop [remaining-maps maps
         current-val seed]
    (cond
      (empty? remaining-maps)
      current-val
      :else
      (recur (rest remaining-maps) (calc-destination current-val (first remaining-maps))))
    ))

(defn calc-result
  "docstring"
  [seeds maps]
  (apply min (map (fn [seed] (calc-location-for-seed seed maps)) seeds)))

(defn parse-seeds
  "docstring"
  [input]
  (map parse-long (rest (str/split (first (str/split-lines input)) #" "))))

(defn parse-map [line]
  (let [[destination-start source-start range] (map parse-long (str/split line #" "))]
    {:destination-start destination-start :source-start source-start :range range}))

(defn parse-maps
  "docstring"
  [input]
  (loop [remaining-lines (rest (str/split-lines input))
         maps []
         current-map []]
    (cond
      (empty? remaining-lines)
      (conj maps current-map)
      (str/includes? (first remaining-lines) "map")
      (if (seq current-map)
        (recur (rest remaining-lines) (conj maps current-map) [])
        (recur (rest remaining-lines) maps current-map))
      :else
      (if (seq (first remaining-lines))
        (recur (rest remaining-lines) maps (conj current-map (parse-map (first remaining-lines))))
        (recur (rest remaining-lines) maps current-map)))))

(def maps (-> "./src/advent_of_code/day5/input.txt"
              slurp
              parse-maps))

(def seeds (parse-seeds (-> "./src/advent_of_code/day5/input.txt"
                            slurp)))



(calc-result seeds maps)