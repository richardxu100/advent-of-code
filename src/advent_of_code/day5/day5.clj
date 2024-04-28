(ns advent-of-code.day5.day5
  (:require [clojure.spec.alpha :as s]))

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

(defn calc-result
  "docstring"
  [seeds maps]
  (let [only-map (first maps)
        seed (first seeds)]
    (calc-destination seed only-map)
    ))
