// src/main/java/org/example/backend/utils/IDGenerator.java
package org.example.backend.util;

import org.springframework.stereotype.Component;

@Component
public class IDGenerator {
    
    /**
     * Generates a new ID based on the last ID, prefix, and total digits.
     *
     * @param lastId The last used ID (e.g., "PA003")
     * @param prefix The prefix for the ID (e.g., "PA")
     * @param digits The number of digits for the numeric part (e.g., 3)
     * @return The next ID in sequence (e.g., "PA004")
     */
    public String generateID(String lastId, String prefix, int digits) {
        try {
            // Ensure the lastId starts with the prefix
            if (!lastId.startsWith(prefix)) {
                throw new IllegalArgumentException("Last ID does not start with the specified prefix.");
            }
            
            // Extract numeric part after the prefix
            String numberPart = lastId.substring(prefix.length());
            int nextNumber = Integer.parseInt(numberPart) + 1;
            
            // Format with leading zeros
            return prefix + String.format("%0" + digits + "d", nextNumber);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            // If there's an error, start with the initial ID
            return prefix + String.format("%0" + digits + "d", 1);
        }
    }
}