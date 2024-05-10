(ns advent-of-code.day7.day7)

(def ranking-map
  {"2" 2
   "3" 3
   "4" 4
   "5" 5
   "6" 6
   "7" 7
   "8" 8
   "9" 9
   "T" 10
   "J" 11
   "Q" 12
   "K" 13
   "A" 14})

(ranking-map "A")

(defn has-pair? [hand]
  (< (count (set hand)) (count hand)))

(defn calc-hand-type
  "docstring"
  [hand]
  (cond
    (has-pair? hand)
    :one-pair
    :else
    :high-card)
  )
