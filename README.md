
Recipe Search # Backend

A set of Rest endpoint to tackle recipe search. 


## Consideration

1. "Provide an endpoint to retrieve a list of recipes created by a specific user" - this requirement coinsiding with "Recipe Search: Username (exact match or partial match)". No other endpoint has been for this, as "/searchRecipeByUserName" can serve both requirement.

2. For a dataset not big enough, using elasticsearch might have been overkill. For now normal Criteria query has been used for search. 

3. Only Authentication has been taken care of for now, 'Role'/'Authority' has not been implemented.  


## API Reference

https://documenter.getpostman.com/view/9935574/2s93sjT8bL

