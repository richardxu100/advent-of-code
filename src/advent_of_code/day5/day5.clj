(ns advent-of-code.day5.day5
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))

;(defn construct-maps
;  "docstring"
;  [input]
;  {:seed-to-soil-map (gen-seed-to-soil-map input)
;   :soil-to-fertilizer-map (gen-soil-to-fertilizer-map input)
;   :fertilizer-to-water-map (gen-fertilizer-to-water-map input)
;   :water-to-light-map (gen-water-to-light-map input)
;   :light-to-temperature-map (gen-light-to-temperature-map input)
;   :temperature-to-humidity-map (gen-temperature-to-humidity-map input)
;   })
;
;(defn calculate-destination-number
;  "docstring"
;  [source, convert-map]
;  ;algorithm. Should fold all of the lists in the convert map. Or idk. Need to do things. Can maybe start smaller?
;  ;also. Maybe don't need to keep track of explicit names of maps. That's kind of arbitrary. So the tests I write can function
;  ;on an arbitrary number of maps. I'll just plug in the input, all of the maps in a row, and we'll see how it goes
;  ;Doing TDD here sounds like the move
;  )

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