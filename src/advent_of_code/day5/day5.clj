(ns advent-of-code.day5.day5 (:require [clojure.spec.alpha :as s]))

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

(defn calc-result
  "docstring"
  [seeds maps]
  (let [offset (first maps)]
    ))
