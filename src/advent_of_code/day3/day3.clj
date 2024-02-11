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
    (loop [remaining row
           current-num ""]
      (cond
        (empty? remaining)
        (if (empty? current-num)
          0
          (Integer/parseInt current-num))
        (is-digit? (first remaining))
        (recur (rest remaining) (str current-num (first remaining)))
        :else
        (recur (rest remaining) current-num)))))