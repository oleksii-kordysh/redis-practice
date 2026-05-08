/**
 * Common utility functions for k6 testing
 * Can be used in any project
 */

/**
 * Generates a random integer in the range [min, max]
 */
export function randomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

/**
 * Generates a random floating-point number in the range [min, max]
 */
export function randomFloat(min, max, decimals = 2) {
    const value = Math.random() * (max - min) + min;
    return Math.round(value * Math.pow(10, decimals)) / Math.pow(10, decimals);
}

/**
 * Generates a random string of the specified length
 */
export function randomString(length = 8) {
    return Math.random().toString(36).substring(2, 2 + length);
}

/**
 * Selects a random element from an array
 */
export function randomChoice(array) {
    return array[Math.floor(Math.random() * array.length)];
}

/**
 * Generates an array of random IDs from a range
 */
export function generateRandomIds(count, minId, maxId) {
    const ids = [];
    for (let i = 0; i < count; i++) {
        ids.push(randomInt(minId, maxId));
    }
    return ids;
}