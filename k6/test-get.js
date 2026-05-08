import http from 'k6/http';
import { sleep } from 'k6';
import { BASE_URL, CACHE_MODE } from './config.js';
import { generateRandomIds, randomChoice } from './utils.js';

const popularIds = generateRandomIds(15, 1, 10000000);

export const options = {
    stages: [
        { duration: '5s', target: 400 },
        { duration: '15s', target: 400 },
        { duration: '5s', target: 0 },
    ],
};

export default function() {
    const productId = randomChoice(popularIds);

    const url = `${BASE_URL}/api/products/${productId}?cacheMode=${CACHE_MODE}`;

    const response = http.get(url);

    if (response.status !== 200 && response.status !== 404) {
        console.log(`GET failed: ${response.status} for ID: ${productId}`);
    } else {
        console.log(`GET success: ${response.status} for ID: ${productId}`);
    }
}