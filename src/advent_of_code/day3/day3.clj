(ns advent-of-code.day3.day3)

(defn is-digit?
  "Check if the given character represents a digit."
  [char]
  (try
    (Integer/parseInt (str char))
    true
    (catch NumberFormatException _
      false)))


(defn sum-of-parts
  "docstring"
  [graph]
  (let [row (first graph)]
    (loop [remaining row]
      (cond
        (empty? remaining)
        0
        (is-digit? (first remaining))
        (Integer/parseInt (first remaining))
        :else
        (recur (rest remaining))))))